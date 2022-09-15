public class Node{

    private int id;
    private String host;
    private int port;

    public Node(int id, String host, int port){
        this.id = id;
        this.host = host;
        this.port = port;
    }

    public int getID(){
        return this.id;
    }

    public String getHost(){
        return this.host;
    }

    public int getPort(){
        return this.port;
    }
    
}
