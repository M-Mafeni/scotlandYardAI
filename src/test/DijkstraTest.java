import org.junit.Test;
import uk.ac.bris.cs.gamekit.graph.*;
import uk.ac.bris.cs.scotlandyard.ui.ai.Dijkstra;
import java.util.*;


public class DijkstraTest{

    //nodes based on pic I sent you
    private void addNodes(Graph<Integer,Integer> testGraph,List<Node<Integer>> nodes){
        for(int a = 1;a <= 5;a++){
            int asc = a + 64; //  to get ASCII code
            testGraph.addNode(new Node<>(asc));
            nodes.add(new Node<>(asc));
        }
    }
    //edges based on pic I sent you
    private void addEdges(Graph<Integer,Integer> testGraph,List<Node<Integer>> nodes){
        testGraph.addEdge(new Edge<>( nodes.get(0),nodes.get(3), 1));
        testGraph.addEdge(new Edge<>(nodes.get(0),nodes.get(1),6));
        testGraph.addEdge(new Edge<>(nodes.get(1),nodes.get(3),2));
        testGraph.addEdge(new Edge<>(nodes.get(1),nodes.get(4),2));
        testGraph.addEdge(new Edge<>(nodes.get(1),nodes.get(2),5));
        testGraph.addEdge(new Edge<>(nodes.get(3),nodes.get(4),1));
        testGraph.addEdge(new Edge<>(nodes.get(2),nodes.get(4),5));
    }

    // to check graph works correctly at the end
    @Test
    public void checkFinalDistances(){
        Graph<Integer,Integer> testGraph = new UndirectedGraph<>();
        List<Node<Integer>> nodes = new ArrayList<>();
        addNodes(testGraph,nodes);
        addEdges(testGraph,nodes);
        Dijkstra testDijkstra = new Dijkstra(testGraph,nodes.get(0));
        Map<Node<Integer>, Integer> distances = testDijkstra.getDistances();
        assert(distances.get(new Node<>(65)) == 0);
        assert(distances.get(new Node<>(66)) == 3);
        assert(distances.get(new Node<>(67)) == 7);
        assert(distances.get(new Node<>(68)) == 1);
        assert(distances.get(new Node<>(69)) == 2);
    }
    @Test
    public void checkPrevNodes(){
        Graph<Integer,Integer> testGraph = new UndirectedGraph<>();
        List<Node<Integer>> nodes = new ArrayList<>();
        addNodes(testGraph,nodes);
        addEdges(testGraph,nodes);
        Dijkstra testDijkstra = new Dijkstra(testGraph,nodes.get(0));
        Map<Node<Integer>,Node<Integer>> prevNodes = testDijkstra.getPrevNodes();
        assert(prevNodes.get(new Node<>(65)) == null);
        assert(prevNodes.get(new Node<>(66)).equals(new Node<>(68)));
        assert(prevNodes.get(new Node<>(67)).equals(new Node<>(69)));
        assert(prevNodes.get(new Node<>(68)).equals(new Node<>(65)));
        assert(prevNodes.get(new Node<>(69)).equals(new Node<>(68)));
    }


}