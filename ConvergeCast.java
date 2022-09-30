import java.util.*;

public class ConvergeCast {

    public static int parentId;

    public static void getParentId(MAP_Protocal map){
        int my_id = map.node_id;
        if(my_id==0){
            // node 0 does not need to send state messsage
            parentId = 0;
            return;
        }
        HashMap<Node, ArrayList<Node>> adj_list = map.neighbor_list;
        int number_of_nodes = map.NUMBER_OF_NODES;
        ArrayList<Integer> visited_node = new ArrayList<>();
        int index = 0;
        visited_node.add(0);

        boolean[] visited = new boolean[number_of_nodes];
        visited[0] = true;
        while(index<visited_node.size()){
            for(Node node: adj_list.get(map.node_list.get(visited_node.get(index)))){
                if(visited[node.getID()])
                    continue;
                if(node.getID() == my_id){
                    parentId = visited_node.get(index);
                    return;
                }
                visited_node.add(node.getID());
                visited[node.getID()] = true;
            }
            index++;
        }
    }

}
