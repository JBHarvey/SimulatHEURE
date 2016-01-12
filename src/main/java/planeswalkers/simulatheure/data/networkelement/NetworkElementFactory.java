/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.networkelement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import planeswalkers.simulatheure.data.SimulationElement;
import planeswalkers.simulatheure.data.units.Coordinates;
import planeswalkers.simulatheure.data.units.TriangularDistribution;

/**
 * NetworkElementFactory class, will contain and manage the different network
 * components
 *
 * @author Jean-Benoit Harvey
 */
public class NetworkElementFactory implements Serializable {

    private String name;
    private HashMap<String, HashMap<Integer, SimulationElement>> networkElements;
    private final String SEGMENT = Segment.class.getSimpleName();
    private final String STATION = Station.class.getSimpleName();
    private final String INTERSECTION = Intersection.class.getSimpleName();

    /**
     * Network constructor, creates network object.
     *
     * @param networkName The name of the network, used to identify it. It also
     * is the name of the saved file.
     */
    public NetworkElementFactory(String networkName) {
        name = networkName;
        networkElements = new HashMap<>();
        initialiseElementContainers();
    }

    private void initialiseElementContainers() {

        HashMap<Integer, SimulationElement> intersections = new HashMap<>();
        HashMap<Integer, SimulationElement> stations = new HashMap<>();
        HashMap<Integer, SimulationElement> segments = new HashMap<>();

        networkElements.put(INTERSECTION, intersections);
        networkElements.put(STATION, stations);
        networkElements.put(SEGMENT, segments);
    }

    /**
     * Allows to retrieve the nodes of the network.
     *
     * @return An HashMap containing the nodes and their identifiers.
     */
    public HashMap<Integer, SimulationElement> getNodes() {
        HashMap<Integer, SimulationElement> nodes = new HashMap<>();
        nodes.putAll(networkElements.get(INTERSECTION));
        nodes.putAll(networkElements.get(STATION));
        return nodes;
    }

    /**
     * Add a new station to the network.
     *
     * @param coordinates The position of the station.
     */
    public void createStation(Coordinates coordinates) {
        String defaultName = createRelativeName(STATION);

        Station newStation = new Station(defaultName, coordinates);
        networkElements.get(STATION).put(newStation.getIdentifier(), newStation);
    }

    /**
     * Add a new intersection to the network.
     *
     * @param coordinates
     */
    public void createIntersection(Coordinates coordinates) {
        String defaultName = createRelativeName(INTERSECTION);

        Intersection newIntersection = new Intersection(defaultName, coordinates);
        networkElements.get(INTERSECTION).put(newIntersection.getIdentifier(), newIntersection);
    }

    public void createSegment(int origin, int end) {
        if (!existsASegmentAt(origin, end)) {
            String defaultName = createRelativeName(SEGMENT);
            TriangularDistribution distribution = new TriangularDistribution(180, 300, 600);
            Segment newSegment = new Segment(defaultName, origin, end, distribution);
            networkElements.get(SEGMENT).put(newSegment.getIdentifier(), newSegment);
        }
    }

    private boolean existsASegmentAt(int origin, int end) {
        Segment existinSegment;
        boolean oneWithSameNodes = false;
        for (Iterator<SimulationElement> it = networkElements.get(SEGMENT).values().iterator(); it.hasNext() && !oneWithSameNodes;) {
            existinSegment = (Segment) it.next();
            if (existinSegment.getOrigin() == origin && existinSegment.getEnd() == end) {
                oneWithSameNodes = true;
            }
        }
        return oneWithSameNodes;
    }

    private String createRelativeName(String className) {
        int relativeNameCounter = networkElements.get(className).size() + 1;
        String newElementName = className + relativeNameCounter;

        return newElementName;
    }

    public <S extends SimulationElement> void modifyNetworkElement(int elementID, S element) {
        networkElements.get(element.getClass().getSimpleName()).replace(elementID, element);
    }

    public <N extends Node> void changeNodeType(int elementID, String oldType, String newType) {
        N oldNode;
//        = networkElements.get(oldType).get(elementID);
    }

    /**
     * Allows to remove a station from the network.
     *
     * @param elementID The identifier of the network element to remove.
     */
    public void removeStation(int elementID) {
        networkElements.get(STATION).remove(elementID);
    }

    /**
     * Allows to remove an intersection from the network.
     *
     * @param elementID The identifier of the network element to remove.
     */
    public void removeIntersection(int elementID) {
        networkElements.get(INTERSECTION).remove(elementID);
    }

    /**
     * Allows to remove a segment from the network.
     *
     * @param elementID The identifier of the network element to remove.
     */
    public void removeSegment(int elementID) {
        networkElements.get(SEGMENT).remove(elementID);
    }

    public HashMap<String, HashMap<Integer, SimulationElement>> getNetworkElements() {
        return networkElements;
    }

    public void setNetworkElements(HashMap<String, HashMap<Integer, SimulationElement>> networkElements) {
        this.networkElements = networkElements;
    }

}
