import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MAP_Protocal {

    int node_id;
    int NUMBER_OF_NODES;
    int min_per_active;
    int max_per_active;
    int min_send_delay;
    int snapshot_delay;
    int max_number;
    boolean active;
    int msg_sent;
    ServerSocket serverSocket;
    HashMap<Integer, Node> node_list = new HashMap<>();
    HashMap<Node, ArrayList<Node>> neighbor_list = new HashMap<>();
    HashMap<Node, Socket> channels = new HashMap<>();

    public MAP_Protocal(int node_id, String config_filename){
        this.node_id = node_id;
        active = false;
        msg_sent = 0;
        readConfig(config_filename);
    }

    public void launchServer() throws IOException{
        serverSocket = new ServerSocket(node_list.get(node_id).getPort());
        Server server = new Server(this);
        new Thread(server).start();
    }

    public void launchClient() throws InterruptedException{
        for(Node neighbor: neighbor_list.get(node_list.get(node_id))){
            System.out.println("Connecting to node "+neighbor.getID() + "...");
            while(true){
                try{
                    channels.put(neighbor, new Socket(neighbor.getHost(),neighbor.getPort()));
                    System.out.println("Node "+neighbor.getID()+" connected!");
                    break;
                }catch(IOException e){
                    System.out.println("Connection failed. Trying to reconnect after 1 sec...");
                    Thread.sleep(1000);
                }
            }
        }
        System.out.println("All neighbors connected!");
        Client client = new Client(this);
        new Thread(client).start();
    }


    private void readConfig(String config_filename){
        try{
            File config = new File(config_filename);
            Scanner scanner = new Scanner(config);
            int valid_line_counter = 0;
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                if(line.length()>0 && Character.isDigit(line.charAt(0))){
                    // this is a valid line
                    valid_line_counter++;
                    line = line.split("#")[0].trim();
                    if(valid_line_counter==1){
                        // read global variable
                        String[] global_var = line.split(" ");
                        this.NUMBER_OF_NODES = Integer.parseInt(global_var[0]);
                        this.min_per_active = Integer.parseInt(global_var[1]);
                        this.max_per_active = Integer.parseInt(global_var[2]);
                        this.min_send_delay = Integer.parseInt(global_var[3]);
                        this.snapshot_delay = Integer.parseInt(global_var[4]);
                        this.max_number = Integer.parseInt(global_var[5]);
                    }
                    else if(valid_line_counter>1 && valid_line_counter<=NUMBER_OF_NODES+1){
                        // read node information
                         String[] node_info = line.split(" ");
                         int id = Integer.parseInt(node_info[0]);
                         String host = node_info[1];
                         int port = Integer.parseInt(node_info[2]);
                         node_list.put(id, new Node(id,host,port));
                         neighbor_list.put(node_list.get(id), new ArrayList<Node>());
                    }   
                    else{
                        // read node neighbor list
                        int current_node_id = valid_line_counter - 2 - NUMBER_OF_NODES;
                        Node current_node = node_list.get(current_node_id);
                        String[] neighbor_id_list = line.split(" ");
                        for(String neighbor_id: neighbor_id_list){
                            neighbor_list.get(current_node).add(node_list.get(Integer.parseInt(neighbor_id)));
                        }
                    }
                }
            }
            scanner.close();
        }catch(Exception e){
            System.out.println("Something went wrong when reading config file");
            e.printStackTrace();
        }
    }
    
}