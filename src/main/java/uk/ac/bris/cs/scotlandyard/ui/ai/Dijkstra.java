package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.*;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.*;

public class Dijkstra {
    private int INF = 1000;
    private Graph<Integer,Transport> graph;
    private Node<Integer> sourceNode;
    private Set<Node<Integer>> unvisited,visited;
    private Map<Node<Integer>,Integer> distances = new HashMap<>(); //map of nodes to their distances

    public Dijkstra(Graph<Integer,Transport> gameMap,Node<Integer> sourceNode){
        this.sourceNode = sourceNode;
        visited = new HashSet<>(); //no nodes are yet to be visited
        unvisited = new HashSet<>(gameMap.getNodes()); //all nodes are unvisited at the start
        graph = gameMap;
        initDistances(distances,unvisited);
        //For now let's assume that all edges have a weight of 1
    }
    private void initDistances(Map<Node<Integer>,Integer> map,Set<Node<Integer>> unvisited){
        //distances from source node should be infinity at start
        for(Node<Integer> u: unvisited){
            if(u.equals(sourceNode)) //distance from source node should be zero at first
                map.put(u,0);
            else
                map.put(u,INF); //distance from all other nodes should be infinity
        }
    }


    public Map<Node<Integer>, Integer> getDistances() {
        return distances;
    }

    public Node<Integer> getSourceNode() {
        return sourceNode;
    }
}

