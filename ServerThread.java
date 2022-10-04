import java.io.ObjectInputStream;
import java.net.Socket;
import java.io.*;

public class ServerThread implements Runnable {

    private Socket client;
    private MAP_Protocal map_protocal;
    
    public ServerThread(MAP_Protocal map_protocal,Socket client){
        this.map_protocal = map_protocal;
        this.client = client;
    }

    public void forwardStatusMsg(Message message){
        try{
            System.out.println("Forwarding status message");
            synchronized(map_protocal){
                Socket client = map_protocal.channels.get(map_protocal.node_list.get(ConvergeCast.parentId));
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                out.writeObject(message);
                out.flush();
            }
        }catch(IOException e){
            System.out.println("Error in forward message");
        }
    }

    @Override
    public void run(){
        while(true){
        try{
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            Message msg = (Message)in.readObject();
            if(msg.getContent().equals("marker")){
                System.out.println("Received marker message.");
                // this is a marker message
                synchronized(map_protocal){
                    map_protocal.marker_mode = true;
                    map_protocal.marker_count++;
                }
            }
            else if(msg.getContent().equals("status")){
                System.out.println("Received status message.");
                // this is a status message
                if(map_protocal.node_id!=0){
                    forwardStatusMsg(msg);
                }
                else{
                    synchronized(map_protocal){
                        map_protocal.status_messages.put(msg.getSourceId(),msg);
                    }
                }
            }
            else if(msg.getContent().equals("halt")){
                System.out.println("Received halt message");
                synchronized(map_protocal){
                    map_protocal.halt_count++;
                    if(!map_protocal.terminated){
                        map_protocal.terminated = true;
                        // first time receives halt message, send halt messages to all neighbors
                        for(Node node: map_protocal.neighbor_list.get(map_protocal.node_list.get(map_protocal.node_id))){
                            Message haltMessage = new Message(map_protocal.node_id,"halt",null);
                            Socket client = map_protocal.channels.get(node);
                            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                            out.writeObject(haltMessage);
                            out.flush();
                        }
                    }
                    if(map_protocal.halt_count == map_protocal.neighbor_list.get(map_protocal.node_list.get(map_protocal.node_id)).size()){
                        // received all halt message from neighbors
                        System.out.println("--------------------------");
                        System.out.println("Server Thread Halted");
                        System.out.println("--------------------------");
                        break;
                    }
                }
            }
            else{
                // this is a application message
                synchronized(map_protocal){
                    map_protocal.msg_receive+=1;
                    int num_nodes = map_protocal.NUMBER_OF_NODES;
                    for(int i =0;i<num_nodes;i++){
                        if(msg.getTimeStamp()[i]>map_protocal.timestamp[i]){
                            map_protocal.timestamp[i]=msg.getTimeStamp()[i];
                        }
                    }
                    System.out.println(map_protocal.node_id+" received application message "+msg.getContent()+" from "+msg.getSourceId()+"");
                    if(!map_protocal.active && map_protocal.msg_sent<map_protocal.max_number){
                        System.out.println("Node is now active");
                        map_protocal.active=true;      
                    }
                }
            }

        }catch(Exception e){
            System.out.println("Server thread error");
        }
        }
    }
    
}
