package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.*;
import uk.ac.bris.cs.scotlandyard.model.Transport;

import java.util.*;

// An implementation of Dijkstra's algorithm to find the shortest node from the source node to each node.

public class Dijkstra {
    private int INF = 1000000000;
    private Graph<Integer,Integer> graph;
    private Node<Integer> sourceNode;
    private Set<Node<Integer>> unvisited;
    private Map<Node<Integer>,Integer> distances = new HashMap<>(); //map of nodes to their distances
    private Map<Node<Integer>,Node<Integer>> prevNodes = new HashMap<>(); //map of nodes to previous nodes so we could get the shortest path

    public Dijkstra(Graph<Integer,Integer> gameMap,Node<Integer> sourceNode){
        this.sourceNode = sourceNode;
        unvisited = new HashSet<>(gameMap.getNodes()); //all nodes are unvisited at the start
        graph = gameMap;
        initDistances();
        initPrevNode();
        while (unvisited.size()> 0) { // while there are still unvisited nodes
            visitUnknown();
        }
    }

    // Visits unvisited node with shortest distance from the source node
    private void visitUnknown(){
        // Unvisited node with shortest distance from source node
        Node<Integer> currentNode = getUnvisitedNode();
        Collection<Edge<Integer,Integer>> edges = graph.getEdgesFrom(currentNode);
        for(Edge<Integer,Integer> e: edges){
            int distance = distances.get(currentNode); // you want the distance from the
                                                      // current node to its unvisited neighbours
            Node<Integer> neighbour = e.destination();
            if(unvisited.contains(neighbour)){ //if the neighbouring node is unvisited
                distance += e.data(); //add the distance from
                //if distance gotten is less than distance stored update it
                if(distance < distances.get(neighbour)){
                    distances.put(neighbour,distance);
                    prevNodes.put(neighbour,currentNode);
                }
            }
        }
        unvisited.remove(currentNode); // remove current node from unvisited
    }

    // Gets the unvisited node with the shortest distance from source node
    private Node<Integer> getUnvisitedNode(){
        int min = INF;
        Node<Integer> minNode = null;
        for(Node<Integer> u: unvisited){
            if(distances.get(u) < min) {
                minNode = u;
            }
        }
        if(minNode == null)
            throw new NullPointerException("error minNode doesn't exist");
        return minNode;
    }

    // Initialises the distance map
    private void initDistances(){
        //distances from source node should be infinity at start
        for(Node<Integer> u: unvisited){
            if(u.equals(sourceNode)) //distance from source node should be zero at first
                distances.put(u,0);
            else
                distances.put(u,INF); //distance from all other nodes should be infinity
        }
    }

    // Initialises the prev Nodes map
    private void initPrevNode(){
        for(Node<Integer> u: unvisited){
            prevNodes.put(u,null); //no other nodes were previously visited
        }
    }

    // Get distance from any node
    public int getDistanceFrom(Node<Integer> destination){
        if(!distances.containsKey(destination))
            throw new IllegalArgumentException("node doesn't exist");
        return distances.get(destination);
    }

    // Returns the shortest distance from the source node to the given node
    public Map<Node<Integer>, Integer> getDistances() {
        return distances;
    }

    // Returns the node previously accessed for a given node
    public Map<Node<Integer>, Node<Integer>> getPrevNodes() {
        return prevNodes;
    }

    // Returns the source node
    public Node<Integer> getSourceNode() {
        return sourceNode;
    }

    // Takes in a graph for Scotland Yard and weighs the edges so you can calculate the distance
    static Graph<Integer,Integer> weighGraph(Graph<Integer, Transport> graph){
        Graph<Integer,Integer> weightedGraph = new UndirectedGraph<>();
        List<Node<Integer>> nodes = graph.getNodes(); //gets all the nodes in game graph
        Collection<Edge<Integer,Transport>> edges = graph.getEdges(); //get all possible edges from a specific node
        //add all nodes to weighted graph
        for(Node<Integer> n : nodes){
            weightedGraph.addNode(n);
        }
        for(Edge<Integer,Transport> e : edges){
            //you shouldn't add ferry edges as detectives cannot use them
            if(!e.data().equals(Transport.FERRY)){
                int weight = getWeight(e.data());
                weightedGraph.addEdge(new Edge<>(e.source(),e.destination(),weight));//all edges are weighted at 1
            }
        }
        return weightedGraph;
    }

    // Returns a weight for the specified transport
    private static int getWeight(Transport x){
        int weight = 0;
        switch (x){
            case TAXI:
                weight = 1;
                break;
            case BUS:
                weight = 1;
                break;
            case UNDERGROUND:
                weight = 1;
        }
        return weight;
    }
}

