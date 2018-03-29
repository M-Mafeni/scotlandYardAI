import org.junit.Test;
import uk.ac.bris.cs.gamekit.graph.*;
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.ui.ai.Dijkstra;

import static uk.ac.bris.cs.scotlandyard.model.Transport.TAXI;
import java.util.*;


public class DijkstraTest{
    @Test
    public void testInitialDistances(){
        Graph<Integer,Transport> testGraph = new UndirectedGraph<>();
        List<Node<Integer>> nodes = new ArrayList<>();
        addNodes(testGraph,nodes);
        addEdges(testGraph,nodes);
        Dijkstra testDijkstra = new Dijkstra(testGraph,nodes.get(0));
        checkDistances(testDijkstra.getDistances(),testDijkstra.getSourceNode());

    }
    private void addNodes(Graph<Integer,Transport> testGraph,List<Node<Integer>> nodes){
        for(int a = 1;a <= 5;a++){
            testGraph.addNode(new Node<>(a));
            nodes.add(new Node<>(a));
        }
    }
    private void addEdges(Graph<Integer,Transport> testGraph,List<Node<Integer>> nodes){
        testGraph.addEdge(new Edge<>( nodes.get(0),nodes.get(3), TAXI));
        testGraph.addEdge(new Edge<>(nodes.get(0),nodes.get(1),TAXI));
        testGraph.addEdge(new Edge<>(nodes.get(1),nodes.get(3),TAXI));
        testGraph.addEdge(new Edge<>(nodes.get(1),nodes.get(4),TAXI));
        testGraph.addEdge(new Edge<>(nodes.get(1),nodes.get(2),TAXI));
        testGraph.addEdge(new Edge<>(nodes.get(3),nodes.get(4),TAXI));
        testGraph.addEdge(new Edge<>(nodes.get(2),nodes.get(4),TAXI));
    }
    private void checkDistances(Map<Node<Integer>, Integer> distances,Node<Integer> sourceNode){
        //checks distances initially
        Set<Node<Integer>> nodes = distances.keySet();
        for(Node<Integer> n:nodes){
            if(n.equals(sourceNode))
                assert(distances.get(n) == 0);
            else
                assert(distances.get(n) == 1000); // Value of INF
        }
    }
}