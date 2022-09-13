import java.io.Serializable;

public class Message implements Serializable{

    private String content; // content of message
    private int source_id;  // id of sender node

    public Message(int source_id, String content){
        this.content = content;
        this.source_id = source_id;
    }

    public String getContent(){
        return content;
    }

    public int getSourceId(){
        return source_id;
    }

}