/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.util.ArrayList;
import planeswalkers.simulatheure.data.transit.TransportNeed;

/**
 *
 * @author Jean-Benoît
 */
public class TransportNeedImage extends TransitImage {

    private TransportNeed transportNeed;
    private ArrayList<CircuitFractionImage> segmentsOrganizedByCircuit;

    public TransportNeedImage(TransportNeed transportNeed,
            ArrayList<CircuitFractionImage> segmentsOrganizedByCircuit) {
        super(transportNeed);
        this.transportNeed = transportNeed;
        this.segmentsOrganizedByCircuit = segmentsOrganizedByCircuit;
        elementShape = new Viewport(-1, -1, 0, 0);
    }

    public ArrayList<CircuitFractionImage> getSegmentsOrganizedByCircuit() {
        return segmentsOrganizedByCircuit;
    }
}
