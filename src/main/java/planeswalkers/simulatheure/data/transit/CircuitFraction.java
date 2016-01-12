/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.transit;

import java.util.ArrayList;

/**
 *
 * @author Jean-Benoit Harvey
 */
public class CircuitFraction {

    private final int circuitID;
    protected final ArrayList<Integer> segments;

    public CircuitFraction(int circuitID, ArrayList<Integer> segments) {
        this.circuitID = circuitID;
        this.segments = segments;
    }

    public int getCircuitID() {
        return circuitID;
    }

    public ArrayList<Integer> getSegments() {
        return segments;
    }
}
