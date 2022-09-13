import java.util.*;
import java.io.*;

public class Runner {

    HashMap<Integer, Node> node_list;

    public Runner(){
        node_list = new HashMap<>();
    }

    public void printNodes(){
        for(Map.Entry<Integer, Node> entry: node_list.entrySet()){
            Node node = entry.getValue();
            System.out.println(node.getID()+" "+node.getHost()+" "+node.getPort());
        }
    }

    public void readConfigFile(String filename){
        try{
            File config = new File(filename);
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
                        Global.NUMBER_OF_NODES = Integer.parseInt(global_var[0]);
                        Global.min_per_active = Integer.parseInt(global_var[1]);
                        Global.max_per_active = Integer.parseInt(global_var[2]);
                        Global.min_send_delay = Integer.parseInt(global_var[3]);
                        Global.snapshot_delay = Integer.parseInt(global_var[4]);
                        Global.max_number = Integer.parseInt(global_var[5]);
                    }
                    else if(valid_line_counter>1 && valid_line_counter<=Global.NUMBER_OF_NODES+1){
                        // read node information
                         String[] node_info = line.split(" ");
                         int id = Integer.parseInt(node_info[0]);
                         String host = node_info[1];
                         int port = Integer.parseInt(node_info[2]);
                         node_list.put(id, new Node(id,host,port));
                    }   
                    else{
                        // read node neighbor list
                        int current_node_id = valid_line_counter - 2 - Global.NUMBER_OF_NODES;
                        Node current_node = node_list.get(current_node_id);
                        String[] neighbor_id_list = line.split(" ");
                        for(String neighbor_id: neighbor_id_list){
                            current_node.addNeighbor(node_list.get(Integer.parseInt(neighbor_id)));
                        }
                    }
                }
                scanner.close();
            }
        }catch(Exception e){
            System.out.println("Something went wrong when reading config file");
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        //System.out.print("Main function");
        Runner runner = new Runner();
        runner.readConfigFile("config.txt");
        runner.printNodes();
    }

}
