import java.io.ObjectInputStream;
import java.net.Socket;
import java.io.*;

public class ServerThread implements Runnable {

    private Socket socket;
    private Node server_node;
    
    public ServerThread(Node server_node,Socket socket){
        this.server_node = server_node;
        this.socket = socket;
    }

    @Override
    public void run(){
        try{


        }catch(Exception e){
            System.out.println("Server thread error");
        }
    }
    
}
