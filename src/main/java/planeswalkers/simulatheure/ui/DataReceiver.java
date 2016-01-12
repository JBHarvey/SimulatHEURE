/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import planeswalkers.simulatheure.animation.Animation;
import planeswalkers.simulatheure.animation.Report;
import planeswalkers.simulatheure.data.SimulationElement;

/**
 * The DataReceiver is the UI-end of the network data transportation. It
 * receives information from the DataSender and keeps it so the Renderer can
 * show the network elements and animation.
 *
 * @author Jean-Benoît
 */
public class DataReceiver {

    private ConcurrentHashMap<String, HashMap<Integer, SimulationElement>> simulationElements;
    private ArrayList<Animation> animations;
    private Report report;
    private boolean receivedAnimation = false;

    /**
     * The DataReceiver constructor.
     */
    public DataReceiver() {
        simulationElements = new ConcurrentHashMap<>();
        animations = new ArrayList<>();
    }

    /**
     * This method allows to access the network (immobile) elements.
     *
     * @return The different network elements.
     */
    public ConcurrentHashMap<String, HashMap<Integer, SimulationElement>> getSimulationElements() {
        return simulationElements;
    }

    /**
     * This method is called by the DataSender in order to update the different
     * network elements
     *
     * @param simulationElements the current representation of the network
     * (immobile) elements.
     */
    public void updateSimulationElements(HashMap<String, HashMap<Integer, SimulationElement>> simulationElements) {
        this.simulationElements.putAll(simulationElements);
    }

    public void updateAnimations(ArrayList<Animation> newAnimations, Report report) {
        animations = newAnimations;
        this.report = report;
        receivedAnimation = true;
    }

    public boolean isReceivedAnimation() {
        return receivedAnimation;
    }

    public ArrayList<Animation> getAnimations() {
        receivedAnimation = false;
        return animations;
    }

    public Report getReport() {
        return report;
    }
}
