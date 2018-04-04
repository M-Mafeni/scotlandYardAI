package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.gamekit.graph.*;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.*;

public class Dijkstra {
    private int INF = 1000;
    private Graph<Character,Integer> graph;
    private Node<Character> sourceNode;
    private Set<Node<Character>> unvisited,visited;
    private Map<Node<Character>,Integer> distances = new HashMap<>(); //map of nodes to their distances
    private Map<Node<Character>,Node<Character>> prevNodes = new HashMap<>(); //map of nodes to previous nodes so we could get the shortest path

    public Dijkstra(Graph<Character,Integer> gameMap,Node<Character> sourceNode){
        this.sourceNode = sourceNode;
        visited = new HashSet<>(); //no nodes are yet to be visited
        unvisited = new HashSet<>(gameMap.getNodes()); //all nodes are unvisited at the start
        graph = gameMap;
        initDistances();
        initPrevNode();
        while (unvisited.size()> 0) { // while there are still unvisited nodes
            visitUnknown();
        }
        //For now let's assume that all edges have a weight of 1
    }
    //visits unvisited node with shortest distance from the source node
    private void visitUnknown(){
        // unvisited node with shortest distance from source node
        Node<Character> currentNode = getUnvisitedNode();
        Collection<Edge<Character,Integer>> edges = graph.getEdgesFrom(currentNode);
        for(Edge<Character,Integer> e: edges){
            int distance = distances.get(currentNode); // you want the distance from the
                                                      // current node to its unvisited neighbours
            Node<Character> neighbour = e.destination();
            if(unvisited.contains(neighbour)){ //if the neighbouring node is unvisited
                distance += e.data(); //add the distance from
                //if distance gotten is less than distance stored update it
                if(distance < distances.get(neighbour)){
                    distances.put(neighbour,distance);
                    prevNodes.put(neighbour,currentNode);
                }
            }
        }
        visited.add(currentNode); //add current node to visited
        unvisited.remove(currentNode); // remove current node from unvisited
    }

    //get unvisited node by finding shortest distance from source node
    private Node<Character> getUnvisitedNode(){
        int min = INF;
        Node<Character> minNode = null;
        for(Node<Character> u: unvisited){
            if(distances.get(u) < min) {
                minNode = u;
            }
        }
        return minNode;
    }
    //initialises the distance map
    private void initDistances(){
        //distances from source node should be infinity at start
        for(Node<Character> u: unvisited){
            if(u.equals(sourceNode)) //distance from source node should be zero at first
                distances.put(u,0);
            else
                distances.put(u,INF); //distance from all other nodes should be infinity
        }
    }
    //initialises the prev Nodes map
    private void initPrevNode(){
        for(Node<Character> u: unvisited){
            prevNodes.put(u,null);
        }
    }


    public Map<Node<Character>, Integer> getDistances() {
        return distances;
    }

    public Map<Node<Character>, Node<Character>> getPrevNodes() {
        return prevNodes;
    }

    public Node<Character> getSourceNode() {
        return sourceNode;
    }
}

