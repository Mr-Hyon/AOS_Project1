import java.io.*;
import java.util.*;
import java.net.Socket;
import java.lang.*;

public class Snapshot implements Runnable{

    private MAP_Protocal map_Protocal;
    int id;
    Node current_node;
    boolean terminated = false;
    String config_file;

    public Snapshot(MAP_Protocal map_Protocal){
        this.map_Protocal = map_Protocal;
        config_file = map_Protocal.config_file;
        id = map_Protocal.node_id;
        current_node = map_Protocal.node_list.get(id);
    }

    @Override
    public void run(){
        while(!terminated){
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
                try{
                    sendMarkerMessage();
                }catch(IOException e){
                    e.printStackTrace();
                }
                int marker_count = 0;
                // wait until receives marker messages from all neighbors
                while(marker_count<map_Protocal.neighbor_list.get(current_node).size()){
                    marker_count = map_Protocal.marker_count;
                }
                if(id!=0)
                    try{
                        sendStatusMessage();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                synchronized(map_Protocal){
                    // this snapshot has ended
                    map_Protocal.marker_count = 0;
                    map_Protocal.marker_mode = false;
                }
                if(id==0){
                    System.out.println("Waiting for all status message to arrive");
                    int size = 0;
                    while(size<map_Protocal.NUMBER_OF_NODES-1){
                        // keep waiting for all status message to arrive
                        synchronized(map_Protocal){
                            size = map_Protocal.status_messages.size();
                        }
                    }
                    checkTermination();
                    synchronized(map_Protocal){
                        map_Protocal.status_messages = new HashMap<>();
                    }
                    try{
                        Thread.sleep(map_Protocal.snapshot_delay);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
            synchronized(map_Protocal){
                terminated = map_Protocal.terminated;
            }
        }
        if(id==0){
            try{
                sendHaltMessage();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void sendStatusMessage() throws IOException{
        synchronized(map_Protocal){
            Message statusMsg = new Message(id,"status",map_Protocal.active,map_Protocal.msg_sent,map_Protocal.msg_receive);
            Socket client = map_Protocal.channels.get(map_Protocal.node_list.get(ConvergeCast.parentId));
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            out.writeObject(statusMsg);
            out.flush();
        }
    }

    public void sendHaltMessage() throws IOException{
        synchronized(map_Protocal){
            // send halt messages to all neighbors
            for(Node node: map_Protocal.neighbor_list.get(current_node)){
                Message haltMessage = new Message(id,"halt",null);
                Socket client = map_Protocal.channels.get(node);
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                out.writeObject(haltMessage);
                out.flush();
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
            write_output(copied_arr);

            // send marker messages to all neighbors
            for(Node node: map_Protocal.neighbor_list.get(current_node)){
                Message markerMessage = new Message(id,"marker",null);
                Socket client = map_Protocal.channels.get(node);
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                out.writeObject(markerMessage);
                out.flush();
            }
        }
    }

    public void write_output(int[] arr){
        // recording local state (timestamp) and write it to output file
        String output_content = "";
        for(int i=0;i<arr.length;i++){
            if(i!=0)
                output_content += " ";
            output_content += arr[i];
        }
        String file_name = config_file+"-"+id+".out";
        File output_file = new File(file_name);
        // check if file exists
        if(!output_file.exists()){
            // create the file if file not exists
            try{
                output_file.createNewFile();
            }catch(IOException e){
                System.out.println("An error occurred when creating output file.");
            } 
        }
        // output the timestamp
        try{
            BufferedWriter output = new BufferedWriter(new FileWriter(file_name,true));
            output.append(output_content);
            output.newLine();
            output.close();
        }catch(IOException e){
            System.out.println("An error occurred when writing output.");
        }
    }

    public void checkTermination(){
        System.out.println("Checking termination status");
        synchronized(map_Protocal){
            int total_send = 0;
            int total_receive = 0;
            total_send += map_Protocal.msg_sent;
            total_receive += map_Protocal.msg_receive;
            for(int i=1;i<=map_Protocal.NUMBER_OF_NODES-1;i++){
                Message msg = map_Protocal.status_messages.get(i);
                if(map_Protocal.active || msg.isActive){
                    System.out.println("---------------------------");
                    System.out.println("Some node is still active!");
                    System.out.println("---------------------------");
                    return;
                }
                total_send += msg.sent_count;
                total_receive += msg.received_count;
            }
            if(total_receive != total_send){
                System.out.println("---------------------------");
                System.out.println("Channels not empty!");
                System.out.println("send:"+total_send);
                System.out.println("receive:"+total_receive);
                System.out.println("---------------------------");
                return;
            }
            map_Protocal.terminated = true;
            System.out.println("---------------------------");
            System.out.println("System is terminated!");
            System.out.println("---------------------------");
        }
    }

}