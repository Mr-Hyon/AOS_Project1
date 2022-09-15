import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    MAP_Protocal map_protocal;

    public Server(MAP_Protocal map_protocal){
        this.map_protocal = map_protocal;
    }

    @Override
    public void run(){
        int neighbor_listened = 0;
        int number_of_neighbor = map_protocal.neighbor_list.get(map_protocal.node_list.get(map_protocal.node_id)).size();
        while(neighbor_listened<number_of_neighbor){
            Socket client;
            try {
                System.out.println("Server thread waiting for connection...");
                client = map_protocal.serverSocket.accept();
                ServerThread thread = new ServerThread(map_protocal, client);
                new Thread(thread).start();
                neighbor_listened++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("All neighbors are connected to server thread");
    }
    
}
