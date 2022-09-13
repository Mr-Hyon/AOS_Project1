import java.io.ObjectInputStream;
import java.net.Socket;
import java.io.*;

public class ServerThread implements Runnable {

    private Socket client;
    private Node server_node;
    
    public ServerThread(Node server_node,Socket client){
        this.server_node = server_node;
        this.client = client;
    }

    @Override
    public void run(){
        while(true){
        try{
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            Message msg = (Message)in.readObject();
            System.out.println("Node "+server_node.getID()+" received message "+msg.getContent()+" from node "+msg.getSourceId()+"");
            if(!server_node.isActive() && server_node.msg_sent<Global.max_number){
                System.out.println("Wake up!");
                //synchronized(server_node){
                    server_node.isActive=true;
                //}
            }

        }catch(Exception e){
            System.out.println("Server thread error");
        }
        }
    }
    
}
