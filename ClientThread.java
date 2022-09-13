import java.io.*;
import java.net.Socket;
import java.lang.Math;

public class ClientThread implements Runnable {

    private Node node;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientThread(Node node){
        this.node = node;
    }

    public void connect(String host, int port) throws IOException{
        clientSocket = new Socket(host, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void sendMessage(String msg){
        out.println(msg);
    }

    @Override
    public void run(){
        
    }

    public void listen(){
        
    }

    
}
