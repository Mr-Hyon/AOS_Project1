import java.io.Serializable;

public class Message implements Serializable{

    private String content; // content of message
    private int source_id;  // id of sender node
    private int [] timestamp;
    public Message(int source_id, String content, int [] timestamp){
        this.content = content;
        this.source_id = source_id;
        if(timestamp != null){
            this.timestamp = new int[timestamp.length];
            for(int i =0;i<timestamp.length;i++){
                this.timestamp[i] = timestamp[i];
            }
        }
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