import java.io.*;
import java.net.Socket;
import java.util.*;
import java.lang.*;

public class ClientThread implements Runnable {

    private Node node;

    public ClientThread(Node node){
        this.node = node;
    }

    public void sendMessage(Message msg, Node target) throws IOException{
        Socket client = node.channels.get(target);
        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        out.writeObject(msg);
        out.flush();
    }

    @Override
    public void run(){
        while(true){
            if(node.isActive){
                    System.out.println("node "+node.getID()+" is active");
                    int msg_to_sent = new Random().nextInt(Global.max_per_active+1-Global.min_per_active)+Global.min_per_active;
                    for(int i=0;i<msg_to_sent;i++){
                        // for each message, randomly choose neighbor as destination
                        System.out.println(node.getID()+" Sending message");
                        ArrayList<Node> neighbor_list = node.getNeighborList();
                        Node target = neighbor_list.get(new Random().nextInt(neighbor_list.size()));
                        Message msg = new Message(node.getID(), "Hello");
                        try {
                            sendMessage(msg, target);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        node.msg_sent++;
                        try {
                            Thread.sleep(Global.min_send_delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    node.isActive = false;
            }
            
        }
    }

    
}
