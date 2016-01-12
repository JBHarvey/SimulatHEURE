/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.util.ArrayList;
import java.util.Iterator;
import planeswalkers.simulatheure.data.transit.Circuit;

/**
 *
 * @author Jean-Benoît
 */
public class CircuitImage extends TransitImage {

    private Circuit circuit;
    private float progressionOffLastBus;

    /**
     *
     * @param circuit
     */
    public CircuitImage(Circuit circuit) {
        super(circuit);
        this.circuit = circuit;
        elementShape = new Viewport(-1, -1, 0, 0);
    }

    public int getBusCapacity() {
        return circuit.getBusCapacity();
    }

    public boolean isLoop() {
        return circuit.isLoop();
    }

    public int getMaxBusNumber() {
        return circuit.getMaxBusNumber();
    }

    public ArrayList<Object> getCurrentSegment(int timeOnCircuit) {
        int segmentTransitTime;
        ArrayList<Object> returnedValues = new ArrayList<>();
        boolean found = false;
        boolean reverse = false;
        SegmentImage currentSegment = null;
        int id;

        for (Iterator<PathImage> it = segments.iterator(); it.hasNext() && !found;) {
            PathImage segmentEntry = it.next();
            currentSegment = segmentEntry.getSegmentImage();
            id = segmentEntry.getSegmentID();
            if (currentSegment.getIdentifier() == id) {
                segmentTransitTime = currentSegment.getTransitTime(id);
                reverse = false;
            } else {
                segmentTransitTime = currentSegment.getTransitTime(currentSegment.getSecondWayId());
                reverse = true;
            }
            if (timeOnCircuit < segmentTransitTime) {
                float progression = ((float) timeOnCircuit) / ((float) segmentTransitTime);
                if (!reverse) {
                    progressionOffLastBus = progression;
                } else {
                    progressionOffLastBus = 1 - progression;
                }
                found = true;
            } else {
                timeOnCircuit -= segmentTransitTime;
            }

        }
        returnedValues.add(currentSegment);
        returnedValues.add(reverse);
        return returnedValues;
    }

    public float getProgressionOffLastBus() {
        return progressionOffLastBus;
    }

}
