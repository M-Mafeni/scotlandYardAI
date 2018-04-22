package dijkstra;

import org.junit.Test;
import uk.ac.bris.cs.gamekit.graph.*;
import uk.ac.bris.cs.scotlandyard.ui.ai.Dijkstra;
import java.util.*;

// Checks that Dijkstra implementation produces the correct graph, distances and previous nodes
public class DijkstraTest{
    // Generate the nodes for the graph
    private void addNodes(Graph<Integer,Integer> testGraph,List<Node<Integer>> nodes){
        for(int a = 1;a <= 5;a++){
            int b = a + 64;
            testGraph.addNode(new Node<>(b));
            nodes.add(new Node<>(b));
        }
    }

    // Generate edges between the nodes for the graph
    private void addEdges(Graph<Integer,Integer> testGraph,List<Node<Integer>> nodes){
        testGraph.addEdge(new Edge<>( nodes.get(0),nodes.get(3), 1));
        testGraph.addEdge(new Edge<>(nodes.get(0),nodes.get(1),6));
        testGraph.addEdge(new Edge<>(nodes.get(1),nodes.get(3),2));
        testGraph.addEdge(new Edge<>(nodes.get(1),nodes.get(4),2));
        testGraph.addEdge(new Edge<>(nodes.get(1),nodes.get(2),5));
        testGraph.addEdge(new Edge<>(nodes.get(3),nodes.get(4),1));
        testGraph.addEdge(new Edge<>(nodes.get(2),nodes.get(4),5));
    }

    // Checks that the shortest distances from the source node are correct
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

    // Checks that the previous node accessed is correct for each node
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