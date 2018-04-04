import org.junit.Test;
import uk.ac.bris.cs.gamekit.graph.*;
import uk.ac.bris.cs.scotlandyard.ui.ai.Dijkstra;
import java.util.*;


public class DijkstraTest{

    //nodes based on pic I sent you
    private void addNodes(Graph<Character,Integer> testGraph,List<Node<Character>> nodes){
        for(int a = 1;a <= 5;a++){
            int asc = a + 64; //  to get ASCII code
            char letter = (char) asc;
            testGraph.addNode(new Node<>(letter));
            nodes.add(new Node<>(letter));
        }
    }
    //edges based on pic I sent you
    private void addEdges(Graph<Character,Integer> testGraph,List<Node<Character>> nodes){
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
        Graph<Character,Integer> testGraph = new UndirectedGraph<>();
        List<Node<Character>> nodes = new ArrayList<>();
        addNodes(testGraph,nodes);
        addEdges(testGraph,nodes);
        Dijkstra testDijkstra = new Dijkstra(testGraph,nodes.get(0));
        Map<Node<Character>, Integer> distances = testDijkstra.getDistances();
        assert(distances.get(new Node<>('A')) == 0);
        assert(distances.get(new Node<>('B')) == 3);
        assert(distances.get(new Node<>('C')) == 7);
        assert(distances.get(new Node<>('D')) == 1);
        assert(distances.get(new Node<>('E')) == 2);
    }
    @Test
    public void checkPrevNodes(){
        Graph<Character,Integer> testGraph = new UndirectedGraph<>();
        List<Node<Character>> nodes = new ArrayList<>();
        addNodes(testGraph,nodes);
        addEdges(testGraph,nodes);
        Dijkstra testDijkstra = new Dijkstra(testGraph,nodes.get(0));
        Map<Node<Character>,Node<Character>> prevNodes = testDijkstra.getPrevNodes();
        assert(prevNodes.get(new Node<>('A')) == null);
        assert(prevNodes.get(new Node<>('B')).equals(new Node<>('D')));
        assert(prevNodes.get(new Node<>('C')).equals(new Node<>('E')));
        assert(prevNodes.get(new Node<>('D')).equals(new Node<>('A')));
        assert(prevNodes.get(new Node<>('E')).equals(new Node<>('D')));

    }


}