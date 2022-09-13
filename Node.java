import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.*;

public class Node implements Runnable{

    private int id;
    private String host;
    private int port;
    private ArrayList<Node> neighbor_list;
    public HashMap<Node, Socket> channels;
    public volatile boolean isActive; // make this volatile, so it will be updated among all threads
    public volatile int msg_sent;
    public ServerSocket serverSocket;

    public Node(int id, String host, int port){
        this.id = id;
        this.host = host;
        this.port = port;
        this.channels = new HashMap<>();
        neighbor_list = new ArrayList<>();
        isActive = false;
        msg_sent = 0;
        try{
            serverSocket = new ServerSocket(port);
        }catch(IOException e){
            System.out.println("fail to establish server socket");
        }
    }

    public void establishChannel(){
        try{
            for(Node neighbor: neighbor_list){
                channels.put(neighbor, new Socket(neighbor.getHost(),neighbor.getPort()));
            }
        }catch(IOException e){
            System.out.println("fail to establish channel");
        }
    }

    public void listen() throws IOException{
        int neighbor_listened = 0;
        while(neighbor_listened<neighbor_list.size()){
            Socket client = serverSocket.accept();
            //System.out.println("Node "+id+" Connection detected");
            ServerThread thread = new ServerThread(this, client);
            new Thread(thread).start();
            neighbor_listened++;
        }
    }

    public void run(){
        ClientThread thread = new ClientThread(this);
        new Thread(thread).start();
        try {
            listen();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean isActive(){
        return this.isActive;
    }

    public void activate(){
        isActive = true;
    }

    public void deactivate(){
        isActive = false;
    }

    public int getID(){
        return this.id;
    }

    public String getHost(){
        return this.host;
    }

    public int getPort(){
        return this.port;
    }

    public void addNeighbor(Node neighbor){
        this.neighbor_list.add(neighbor);
    }

    public ArrayList<Node> getNeighborList(){
        return this.neighbor_list;
    }
    
}
