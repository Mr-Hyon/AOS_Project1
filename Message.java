public class Message{

    private String content;
    private int source_id;

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