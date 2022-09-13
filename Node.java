import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Node implements Runnable{

    private int id;
    private String host;
    private int port;
    private ArrayList<Node> neighbor_list;
    private boolean isActive;
    private int msg_sent;

    public Node(int id, String host, int port){
        this.id = id;
        this.host = host;
        this.port = port;
        neighbor_list = new ArrayList<>();
        isActive = false;
        msg_sent = 0;
    }

    public void runClientThread(){
        while(true){
            if(isActive){

            }
        }
    }

    public void runServerThread() throws IOException{
        ServerSocket ss = new ServerSocket(port);
        while(true){
            Socket client = ss.accept();
            ServerThread thread = new ServerThread(this, client);
            new Thread(thread).start();
        }
    }

    public void run(){

        //System.out.println("Node "+node.getID()+" client thread start running");
        //while(true){
        //    if(node.isActive()){
        //        int random_number_msg = (int)Math.floor(Math.random(Global.max_per_active-Global.min_per_active)*(Global.min_per_active));
        //    }
        //}
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
