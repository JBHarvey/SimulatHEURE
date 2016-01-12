/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import planeswalkers.simulatheure.data.transit.CircuitFraction;

/**
 *
 * @author Jean-Benoît
 */
public class SegmentList extends TextBox {

    protected Text boxTitle;
    protected LinkedHashMap<Text, Integer> segments;
    protected LinkedHashMap<Text, LinkedHashMap<Text, Integer>> circuitParts;
    protected ArrayList<CircuitFraction> transportNeedPath;
    private int stepCounter;

    public SegmentList(String name) {
        super(name + SegmentList.class.getSimpleName(), 0, 0, defaultTextOffsets);
        this.stepCounter = 0;
    }

    public void loadCircuit(ArrayList<PathImage> images) {
        setBoxTitle("Segments empruntés");
        circuitParts = new LinkedHashMap<>();
        segments = createCircuitFraction(images);
        circuitParts.clear();
        circuitParts = null;
        stepCounter = 0;
    }

    private LinkedHashMap<Text, Integer> createCircuitFraction(ArrayList<PathImage> images) {
        LinkedHashMap<Text, Integer> circuitFractions = new LinkedHashMap<>();
        int id;
        SegmentImage segment;
        for (PathImage pathImage : images) {
            segment = pathImage.getSegmentImage();
            id = pathImage.getSegmentID();
            Text fraction = createSegmentText(segment);
            if (segment.getIdentifier() == id) {
                circuitFractions.put(fraction, segment.getIdentifier());
            } else {
                circuitFractions.put(fraction, segment.getSecondWayId());
            }
            addChild(fraction);
        }
        return circuitFractions;
    }

    private Text createSegmentText(SegmentImage segment) {
        final String segmentName = segment.getDisplayName();
        Text fraction = new Text(++stepCounter + segmentName);
        fraction.setTitle("");
        fraction.setSeparator(Text.TABULATION);
        fraction.setContent(segmentName);
        fraction.notEditable();
        return fraction;
    }

    private void setBoxTitle(String textName) {
        boxTitle = new Text(textName);
        boxTitle.setContent("");
        boxTitle.notEditable();
        addChild(boxTitle);
    }

    public void loadTransportNeed(ArrayList<CircuitFractionImage> circuitFractionImages) {
        setBoxTitle("Circuits empruntés");
        circuitParts = new LinkedHashMap<>();
        transportNeedPath = new ArrayList<>();
        segments = new LinkedHashMap<>();

        Text circuitName;
        for (CircuitFractionImage circuitFractionImage : circuitFractionImages) {
            circuitName = createCircuitTextName(circuitFractionImage);
            circuitParts.put(circuitName, createCircuitFraction(circuitFractionImage.getPathImages()));
            transportNeedPath.add(circuitFractionImage.getCircuitFraction());
        }
        segments.clear();
        segments = null;
    }

    private Text createCircuitTextName(CircuitFractionImage circuitFractionImage) {
        String fractionName = circuitFractionImage.getCircuitImage().getDisplayName();
        Text circuit = new Text(++stepCounter + fractionName);
        circuit.setTitle("");
        circuit.setSeparator(" - ");
        circuit.setContent(fractionName);
        circuit.notEditable();
        addChild(circuit);
        return circuit;
    }

    public ArrayList<Integer> getCircuitPath() {
        ArrayList<Integer> path = new ArrayList<>();
        for (Integer identifier : segments.values()) {
            path.add(identifier);
        }
        return path;
    }

    public ArrayList<CircuitFraction> getTransportNeedPath() {
        return transportNeedPath;
    }
}
