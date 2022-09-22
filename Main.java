import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException{
        //args[0] is node_id, args[1] is path to config file
        MAP_Protocal protocal = new MAP_Protocal(Integer.parseInt(args[0]), args[1]);
        protocal.launchServer();
        protocal.launchClient();
        if(protocal.node_id==0)
            protocal.active = true;

        // for(int time : protocal.timestamp){
        //     System.out.println(time);
        // }
        
    }

}
