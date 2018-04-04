import org.junit.Test;
import uk.ac.bris.cs.gamekit.graph.*;
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.ui.ai.Dijkstra;

import static uk.ac.bris.cs.scotlandyard.model.Transport.TAXI;
import java.util.*;


public class DijkstraTest{
    @Test
    public void testInitialDistances(){
        Graph<Character,Integer> testGraph = new UndirectedGraph<>();
        List<Node<Character>> nodes = new ArrayList<>();
        addNodes(testGraph,nodes);
        addEdges(testGraph,nodes);
        Dijkstra testDijkstra = new Dijkstra(testGraph,nodes.get(0));
        checkDistances(testDijkstra.getDistances(),testDijkstra.getSourceNode());

    }
    private void addNodes(Graph<Character,Integer> testGraph,List<Node<Character>> nodes){
        for(int a = 1;a <= 5;a++){
            int asc = a + 64; //  to get ASCII code
            char letter = (char) asc;
            testGraph.addNode(new Node<>(letter));
            nodes.add(new Node<>(letter));
        }
    }
    private void addEdges(Graph<Character,Integer> testGraph,List<Node<Character>> nodes){
        testGraph.addEdge(new Edge<>( nodes.get(0),nodes.get(3), 1));
        testGraph.addEdge(new Edge<>(nodes.get(0),nodes.get(1),6));
        testGraph.addEdge(new Edge<>(nodes.get(1),nodes.get(3),2));
        testGraph.addEdge(new Edge<>(nodes.get(1),nodes.get(4),2));
        testGraph.addEdge(new Edge<>(nodes.get(1),nodes.get(2),5));
        testGraph.addEdge(new Edge<>(nodes.get(3),nodes.get(4),1));
        testGraph.addEdge(new Edge<>(nodes.get(2),nodes.get(4),5));
    }
    private void checkDistances(Map<Node<Character>, Integer> distances,Node<Character> sourceNode){
        //checks distances initially
        Set<Node<Character>> nodes = distances.keySet();
        for(Node<Character> n:nodes){
            if(n.equals(sourceNode))
                assert(distances.get(n) == 0);
            else
                assert(distances.get(n) == 1000); // Value of INF
        }
    }
}