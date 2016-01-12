/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.util.ArrayList;
import planeswalkers.simulatheure.data.transit.CircuitFraction;

/**
 *
 * @author Marc-Antoine Fortier
 */
public class CircuitFractionImage {

    private CircuitImage circuitImage;
    private ArrayList<PathImage> pathImages;

    public CircuitFractionImage(CircuitImage circuitImage, ArrayList<PathImage> pathImages) {
        this.circuitImage = circuitImage;
        this.pathImages = pathImages;
    }

    public CircuitImage getCircuitImage() {
        return circuitImage;
    }

    public ArrayList<PathImage> getPathImages() {
        return pathImages;
    }

    public CircuitFraction getCircuitFraction() {
        ArrayList<Integer> segmentIDs = new ArrayList<>();
        for (PathImage pathImage : pathImages) {
            segmentIDs.add(pathImage.getSegmentID());
        }
        return new CircuitFraction(circuitImage.getIdentifier(), segmentIDs);
    }

}
