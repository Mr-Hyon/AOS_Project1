import java.io.Serializable;

public class Message implements Serializable{

    private String content; // content of message
    private int source_id;  // id of sender node
    private int [] timestamp; // only application message need to include this
    
    // variables for status message
    boolean isActive;
    int sent_count;
    int received_count;

    public Message(int source_id, String content, int [] timestamp){
        this.content = content;
        this.source_id = source_id;
        if(timestamp != null){
            this.timestamp = new int[timestamp.length];
            for(int i =0;i<timestamp.length;i++){
                if(i==source_id)
                    this.timestamp[i] = timestamp[i]+1;
                else
                    this.timestamp[i] = timestamp[i];
            }
        }
    }

    public Message(int source_id, String content, boolean status, int sent_count, int received_count){
        this.content = content;
        this.source_id = source_id;
        this.isActive = status;
        this.sent_count = sent_count;
        this.received_count = received_count;
    }

    public String getContent(){
        return content;
    }

    public int getSourceId(){
        return source_id;
    }
    public int [] getTimeStamp(){
        return timestamp;
    }

}