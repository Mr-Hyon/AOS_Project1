import java.io.IOException;

public class Snapshot implements Runnable{

    private MAP_Protocal map_Protocal;
    int id;
    Node current_node;
    boolean terminated = false;

    public Snapshot(MAP_Protocal map_Protocal){
        this.map_Protocal = map_Protocal;
        id = map_Protocal.node_id;
        current_node = map_Protocal.node_list.get(id);
    }

    @Override
    public void run(){
        while(true){
            if(id==0){
                synchronized(map_Protocal){
                    map_Protocal.marker_mode = true;
                }
            }
            boolean isMarkerMode;
            synchronized(map_Protocal){
                isMarkerMode = map_Protocal.marker_mode;
            }
            if(isMarkerMode){
                sendMarkerMessage();
                int marker_count = 0;
                // wait until receives marker messages from all neighbors
                while(marker_count<map_Protocal.neighbor_list.get(current_node).size()){
                    marker_count = map_Protocal.marker_count;
                }
                synchronized(map_Protocal){
                    // this snapshot has ended
                    map_Protocal.marker_count = 0;
                    map_Protocal.marker_mode = false;
                    // converge cast to inform status to parents

                }
                if(id==0){
                    Thread.sleep(map_Protocal.min_send_delay);
                }
            }
        }
    }

    public void sendMarkerMessage() throws IOException{
        synchronized(map_Protocal){
            // first of all, save its local state (vector clock)
            int[] timestamp = map_Protocal.timestamp;
            int[] copied_arr = new int[timestamp.length];
            for(int i=0;i<timestamp.length;i++){
                copied_arr[i] = timestamp[i];
            }
            map_Protocal.saved_states.add(copied_arr);

            // send marker messages to all neighbors
            for(Node node: map_Protocal.neighbor_list.get(current_node)){
                Message markerMessage = new Message(id,"marker",null);
                Socket client = map_Protocal.channels.get(node);
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                out.writeObject(msg);
                out.flush();
            }
        }
    }

    public void write_output(){
        
    }

    public void checkTermination(){

    }

}