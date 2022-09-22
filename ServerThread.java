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

    @Override
    public void run(){
        while(true){
        try{
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            Message msg = (Message)in.readObject();
            synchronized(map_protocal){
                System.out.println("Node "+map_protocal.node_id+" received message "+msg.getContent()+" from node "+msg.getSourceId()+"");
                if(!map_protocal.active && map_protocal.msg_sent<map_protocal.max_number){
                    map_protocal.msg_receive+=1;
                    int num_nodes = map_protocal.NUMBER_OF_NODES;
                    for(int i =0;i<num_nodes;i++){
                        if(msg.getTimeStamp()[i]>map_protocal.timestamp[i]){
                            map_protocal.timestamp[i]=msg.getTimeStamp()[i];
                        }
                    }

                    System.out.println("Node is now active");
                    map_protocal.active=true;      
                }
            }

        }catch(Exception e){
            System.out.println("Server thread error");
        }
        }
    }
    
}
