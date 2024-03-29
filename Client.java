import java.io.*;
import java.net.Socket;
import java.util.*;
import java.lang.*;

public class Client implements Runnable {

    private MAP_Protocal map_Protocal;
    private boolean terminated = false;

    public Client(MAP_Protocal map_Protocal){
        this.map_Protocal = map_Protocal;
    }

    public void sendMessage(Message msg, Node target) throws IOException{
        Socket client = map_Protocal.channels.get(target);
        map_Protocal.msg_sent+=1;
        map_Protocal.timestamp[map_Protocal.node_id]+=1;  
        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        out.writeObject(msg);
        out.flush();
    }

    @Override
    public void run(){
        while(!terminated){
            boolean isActive;
            synchronized(map_Protocal){
                isActive = map_Protocal.active;
            }
            if(isActive){
                    int msg_to_sent = new Random().nextInt(map_Protocal.max_per_active+1-map_Protocal.min_per_active)+map_Protocal.min_per_active;
                    for(int i=0;i<msg_to_sent;i++){
                        // for each message, randomly choose neighbor as destination
                    synchronized(map_Protocal){
                        ArrayList<Node> neighbor_list = map_Protocal.neighbor_list.get(map_Protocal.node_list.get(map_Protocal.node_id));
                        Node target = neighbor_list.get(new Random().nextInt(neighbor_list.size()));
                        Message msg = new Message(map_Protocal.node_id, "Hello", map_Protocal.timestamp);
                        try {
                            sendMessage(msg, target);
                            System.out.println(map_Protocal.node_id+" sending application message to node "+target.getID());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                        try {
                            Thread.sleep(map_Protocal.min_send_delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    synchronized(map_Protocal){
                        map_Protocal.active = false;
                    }
                    System.out.println("Node is now passive.");
                    //if(map_Protocal.msg_sent>=map_Protocal.max_number)
                    //    System.out.println("this node is now permanently passive.");
            }
            synchronized(map_Protocal){
                terminated = map_Protocal.terminated;
            }  
        }
        System.out.println("-----------------------");
        System.out.println("Protocal is terminated. Client thread halted.");
        System.out.println("-----------------------");
    }

    
}
