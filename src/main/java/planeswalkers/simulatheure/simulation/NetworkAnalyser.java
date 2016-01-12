/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import planeswalkers.simulatheure.data.SimulationElement;
import planeswalkers.simulatheure.data.networkelement.Segment;
import planeswalkers.simulatheure.data.transit.Circuit;

/**
 * This class makes sure the selected circuit is doable. It simply parses the
 * selected segments and nodes to make sure the given sequence actually forms
 * continuous a path.
 *
 * @author Jean-Benoit Harvey
 */
public class NetworkAnalyser {

    private HashMap<Integer, ArrayList<PathEnd>> network;
    private HashMap<Integer, Double> distances;
    private HashMap<Integer, HashMap<Integer, Integer>> previousNodeNetwork;
    private HashMap<Integer, Integer> previousNode;
    private Circuit lockedCircuit;
    private boolean lockedOnCircuit = false;

    private HashMap<ArrayList<Integer>, Integer> storedPathCost;

    private NetworkAnalyser() {
    }

    /**
     * Generates the node neighbourhood from which Djikstra will process the
     * paths.
     *
     * @param graph The sent HashMap must be the Segment one.
     */
    public void refreshNetwork(HashMap<Integer, SimulationElement> graph) {
        network = new HashMap<>();
        previousNodeNetwork = new HashMap<>();
        storedPathCost = new HashMap<>();
        if (!lockedOnCircuit) {
            for (SimulationElement se : graph.values()) {
                addArc((Segment) se);
            }
        } else {
            for (Integer segmentID : lockedCircuit.getPath()) {
                System.out.println(segmentID);
                addArc((Segment) graph.get(segmentID));
            }
        }
    }

    /**
     * Locks the djikstra selection for the selected circuit only, thus no other
     * node can be selected.
     *
     * @param circuit the selected circuit
     */
    public void lockOnCircuit(Circuit circuit) {
        lockedCircuit = circuit;
        lockedOnCircuit = true;
    }

    public void removeLock() {
        lockedCircuit = null;
        lockedOnCircuit = false;
    }

    private void addArc(Segment newLink) {
        int origin = newLink.getOrigin();
        int end = newLink.getEnd();
        addNode(origin);
        addNode(end);
        double weight = newLink.getTriangularDistribution().getMean();
        network.get(origin).add(new PathEnd(end, weight, newLink.getIdentifier()));

    }

    private void addNode(int nodeIdentifier) {
        if (!network.containsKey(nodeIdentifier)) {
            ArrayList<PathEnd> neighbours = new ArrayList<>();
            neighbours.add(new PathEnd(nodeIdentifier, 0.0, -1));
            network.put(nodeIdentifier, neighbours);
        }
    }

    public void dijkstra(int source) {

        System.out.println(source);
        generatePreviousNodeMap(source);
        distances = new HashMap<>();
        HashSet<Integer> unsolved = new HashSet<>();

        for (Integer node : network.keySet()) {
            distances.put(node, Double.MAX_VALUE);
            previousNode.put(node, -1);
            unsolved.add(node);
        }
        distances.put(source, 0.0);

        while (!unsolved.isEmpty()) {
            int current = -1;
            for (Integer node : unsolved) {
                if (current == -1) {
                    current = node;
                } else {
                    if (distances.get(current) > distances.get(node)) {
                        current = node;
                    }
                }
            }

            unsolved.remove(current);
            ArrayList<PathEnd> neighbours = network.get(current);
            if (neighbours.size() > 1) {
                for (PathEnd end : neighbours) {
                    double alternativeCost = distances.get(current) + end.getWeight();
                    if (alternativeCost < distances.get(end.identifier)) {
                        distances.replace(end.identifier, alternativeCost);
                        previousNode.put(end.identifier, current);
                    }
                }

            }
        }
    }

    private void generatePreviousNodeMap(int source) {
        if (previousNodeNetwork.containsKey(source)) {
            previousNode = previousNodeNetwork.get(source);
        } else {
            previousNode = new HashMap<>();
        }
    }

    public ArrayList<Integer> getShortestPathInNodes(int source, int destination) {
        if (previousNode.get(destination) == -1) {
            return new ArrayList<>();
        }

        Integer[] reversePath = backTrack(source, destination);
        ArrayList<Integer> path = new ArrayList<>();
        for (int i = reversePath.length - 1; i >= 0; --i) {
            path.add(reversePath[i]);
        }
        storeCost(path);
        return path;
    }

    private Integer[] backTrack(int source, int destination) {

        ArrayList<Integer> backTrackPath = new ArrayList<>();
        int currentNode = destination;
        do {
            backTrackPath.add(currentNode);
            currentNode = previousNode.get(currentNode);
        } while (currentNode != source);
        backTrackPath.add(source);
        Integer array[] = new Integer[backTrackPath.size()];
        return backTrackPath.toArray(array);
    }

    private void storeCost(ArrayList<Integer> path) {
        int currentCost = 0;
        int origin = path.get(0);
        int end;
        for (int i = 1; i < path.size(); i++) {
            end = path.get(i);
            for (PathEnd arcEnd : network.get(origin)) {
                if (arcEnd.getIdentifier() == end) {
                    currentCost += arcEnd.getWeight();
                }
            }
            origin = path.get(i);
        }
        storedPathCost.put(path, currentCost);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer node : network.keySet()) {
            sb.append("\nNode : ").append(node).append("\n");
            for (PathEnd neighbours : network.get(node)) {
                sb.append("    |    ").append(neighbours.identifier).append("  ->  ").append(neighbours.weight).append("\n");
            }
            sb.append("------------------");

        }
        return sb.toString();
    }

    public ArrayList<Integer> getTransitSegmentID(ArrayList<Integer> dijkstraPath) throws IndexOutOfBoundsException {
        ArrayList<Integer> segmentIDs = new ArrayList<>();
        int origin = dijkstraPath.get(0);
        int end;
        for (int i = 1; i < dijkstraPath.size(); ++i) {

            end = dijkstraPath.get(i);
            System.out.println(findSegmentID(origin, end));
            segmentIDs.add(findSegmentID(origin, end));
            origin = dijkstraPath.get(i);
        }
        return segmentIDs;
    }

    private Integer findSegmentID(int origin, int end) {
        for (PathEnd link : network.get(origin)) {
            if (link.getIdentifier() == end) {
                return link.segmentID;
            }
        }
        return -1;
    }

    /**
     * Determines if a node is selectable (they all are if no lock is on )
     *
     * @param nodeID the selected node
     * @return false if it is not lock, then it can be used to find/create a
     * path, true otherwise.
     */
    public boolean isNodeLocked(int nodeID) {
        boolean locked = false;
        if (lockedOnCircuit) {
            locked = !network.containsKey(nodeID);
        }
        return locked;
    }

    /**
     * A simple data structure class to store the ending node of a segment as
     * well as the cost to go to it starting from this node.
     */
    private class PathEnd {

        private final int identifier;
        private final double weight;
        private final int segmentID;

        private PathEnd(int indentifier, double weight, int segmentID) {
            this.identifier = indentifier;
            this.weight = weight;
            this.segmentID = segmentID;
        }

        public int getIdentifier() {
            return identifier;
        }

        public double getWeight() {
            return weight;
        }

    }

    public static NetworkAnalyser getInstance() {
        return NetworkAnalyser.NetworkAnalyserHolder.INSTANCE;

    }

    private static class NetworkAnalyserHolder {

        private static final NetworkAnalyser INSTANCE = new NetworkAnalyser();
    }
}
