/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.transit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import planeswalkers.simulatheure.data.SimulationElement;
import planeswalkers.simulatheure.data.units.TriangularDistribution;

/**
 *
 * @author Antoine
 */
public class TransitFactory implements Serializable {

    private final String name;
    private HashMap<String, HashMap<Integer, SimulationElement>> transits;
    private final TriangularDistribution defaultApparitionDistributionTransportNeed = new TriangularDistribution(000, 60, 300);
    private final TriangularDistribution defaultApparitionDistributionCircuit = new TriangularDistribution(900, 900, 900);
    private final int defaultFirstApparition = 5 * 3600;
    private final String TRANSPORT_NEED = TransportNeed.class.getSimpleName();
    private final String CIRCUITS = Circuit.class.getSimpleName();

    public TransitFactory(String name) {
        this.name = name;
        this.transits = new HashMap<>();
        initialiseElementContainers();
    }

    private void initialiseElementContainers() {
        HashMap<Integer, SimulationElement> circuits = new HashMap<>();
        HashMap<Integer, SimulationElement> transportNeeds = new HashMap<>();
        this.transits.put(CIRCUITS, circuits);
        this.transits.put(TRANSPORT_NEED, transportNeeds);
    }

    public void createCircuit(ArrayList<Integer> selectedSegments) {
        String defaultName = createRelativeName(CIRCUITS);
        int defaultBusCapacity = 50;
        boolean loop = false;
        int defaultNumberOfBusLimit = -1;

        Circuit newCircuit = new Circuit(defaultName, selectedSegments, defaultFirstApparition, defaultBusCapacity, loop, defaultNumberOfBusLimit, defaultApparitionDistributionCircuit);
        this.transits.get(CIRCUITS).put(newCircuit.getIdentifier(), newCircuit);
    }

    public void createTransportNeed(ArrayList<CircuitFraction> selectedCircuits) {
        String defaultName = createRelativeName(TRANSPORT_NEED);
        TransportNeed newTransportNeed = new TransportNeed(defaultName, selectedCircuits, defaultFirstApparition, defaultApparitionDistributionTransportNeed);
        this.transits.get(TRANSPORT_NEED).put(newTransportNeed.getIdentifier(), newTransportNeed);
    }

    public void modifyTransit(int transitID, Transit newTransit) {
        transits.get(newTransit.getClass().getSimpleName()).replace(transitID, newTransit);
    }

    public void removeCircuit(int elementID) {
        this.transits.get(CIRCUITS).remove(elementID);
    }

    public void removeTransportNeed(int elementID) {
        this.transits.get(TRANSPORT_NEED).remove(elementID);
    }

    private String createRelativeName(String className) {
        int relativeNameCounter = this.transits.get(className).size() + 1;
        String newElementName = className + relativeNameCounter;

        return newElementName;
    }

    public HashMap<String, HashMap<Integer, SimulationElement>> getTransits() {
        return this.transits;
    }

    public void setTransits(HashMap<String, HashMap<Integer, SimulationElement>> transits) {
        this.transits = transits;
    }

}
