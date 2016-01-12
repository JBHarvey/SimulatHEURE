/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.ListIterator;
import planeswalkers.simulatheure.data.SimulationElement;
import planeswalkers.simulatheure.data.networkelement.Intersection;
import planeswalkers.simulatheure.data.networkelement.Segment;
import planeswalkers.simulatheure.data.networkelement.Station;
import planeswalkers.simulatheure.data.transit.Circuit;
import planeswalkers.simulatheure.data.transit.TransportNeed;
import planeswalkers.simulatheure.ui.components.Buttons.SquareConfirmButton;
import planeswalkers.simulatheure.ui.gui.AnimationParametersBox;
import planeswalkers.simulatheure.ui.gui.EditionButtons;
import planeswalkers.simulatheure.ui.gui.ElementPositionInfo;
import planeswalkers.simulatheure.ui.gui.SegmentInformationBox;

/**
 *
 * @author Jean-Benoît
 */
public class NetworkEditionBox extends Box<String, Box> {

    private SegmentInformationBox segmentInfo;
    private ElementPositionInfo stationInfo;
    private ElementPositionInfo intersectionInfo;
    private TransitEditionBox circuitInfo;
    private TransitEditionBox transportNeedInfo;
    private AnimationParametersBox animationParameters;
    private int elementInEditionID;

    protected EditionButtons editionButtons;
    protected double boxWidth = 350;

    /**
     * A Box that contains NetworkInformation related boxes and a box containing
     * with a Confim and Cancel button. It allows the user to edit the network
     * easily and it allows us to easily change the information box presented.
     *
     * @param name
     * @param x
     * @param y
     */
    public NetworkEditionBox(String name, double x, double y) {
        super(name, x, y);
        elementShape = new Viewport(relativeX, relativeY, boxWidth, 250);
        verticalPadding = 20;
        horizontalPadding = 20;

        setBackgroundColor(new Color(53, 72, 163));
    }

    public void close() {
        removeIfPresent(editionButtons);
        removeIfPresent(segmentInfo);
        removeIfPresent(stationInfo);
        removeIfPresent(intersectionInfo);
        removeIfPresent(circuitInfo);
        removeIfPresent(transportNeedInfo);
        removeIfPresent(animationParameters);
    }

    protected void removeIfPresent(Box toRemove) {
        if (getContainedElement().containsValue(toRemove)) {
            elementInEditionID = -1;
            removeChild(toRemove);
        }
    }

    public void openSegment(SegmentImage segment) {
        elementInEditionID = segment.getIdentifier();
        segmentInfo = new SegmentInformationBox(segment);
        addChild(segmentInfo);
    }

    public void openStation(StationImage station) {
        elementInEditionID = station.getIdentifier();
        stationInfo = new ElementPositionInfo(Station.class.getSimpleName());
        stationInfo.open(station);
        addChild(stationInfo);
    }

    public void openIntersection(IntersectionImage intersection) {
        elementInEditionID = intersection.getIdentifier();
        intersectionInfo = new ElementPositionInfo(Intersection.class.getSimpleName());
        intersectionInfo.open(intersection);
        addChild(intersectionInfo);
    }

    public void openCircuit(CircuitImage circuit) {
        elementInEditionID = circuit.getIdentifier();
        circuitInfo = new TransitEditionBox(circuit, relativeX, relativeY);
        addChild(circuitInfo);
    }

    public void openTransportNeed(TransportNeedImage transportNeed) {
        elementInEditionID = transportNeed.getIdentifier();
        transportNeedInfo = new TransitEditionBox(transportNeed, relativeX, relativeY);
        addChild(transportNeedInfo);
    }

    public void openAnimationParameters(AnimatorImage animation) {
        animationParameters = new AnimationParametersBox(animation);
        addChild(animationParameters);
    }

    @Override
    public void setRenderProperties(Viewport viewport) {
        relativeX = viewport.getX();
        relativeY = viewport.getY();
        for (Box box : getContainedElement().values()) {
            box.setRenderProperties(viewport);
        }
        addPlusButton();

    }

    protected void addPlusButton() {
        editionButtons = new EditionButtons(relativeX + (boxWidth - 3 * verticalPadding), relativeY + getBoxHeight() + verticalPadding);
        addNeededButtons();
        addChild(editionButtons);
        elementShape = new Viewport(relativeX, relativeY, boxWidth + (2 * horizontalPadding), (getBoxHeight() + editionButtons.getHeight() + verticalPadding));
    }

    protected void addNeededButtons() {
        editionButtons.addButton(new SquareConfirmButton());
    }

    @Override
    public double getBoxHeight() {
        double boxHeight = 2 * verticalPadding;
        for (Box box : getContainedElement().values()) {
            if (box.getBoxHeight() != 0) {
                boxHeight += box.getBoxHeight();
            }
        }
        return boxHeight;
    }

    @Override
    public double getBoxWidth() {
        double boxHeight = 2 * horizontalPadding;
        for (Box box : getContainedElement().values()) {
            if (box.getBoxHeight() != 0) {
                boxHeight += box.getBoxWidth();
            }
        }
        return boxHeight;
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        saveGraphicsConfigurations(graphics2D);

        graphics2D.setColor(getBackgroundColour());
        Viewport frame = new Viewport(elementShape.getX() - horizontalPadding, elementShape.getY() - verticalPadding, elementShape.getWidth() + (2 * horizontalPadding), elementShape.getHeight() + (2 * verticalPadding));
        graphics2D.fill(frame.getShape());
        int size = getContainedElement().size();
        ListIterator<Box> iter = new ArrayList<>(getContainedElement().values()).listIterator(size);
        while (iter.hasPrevious()) {
            iter.previous().paint(graphics2D);
        }

        restoreGraphicsConfigurations(graphics2D);
    }

    public int getElementInEditionID() {
        return elementInEditionID;
    }

    public SimulationElement getModifiedSegment() {
        return (Segment) segmentInfo.getModifiedElement();
    }

    public Segment getSecondSegment() {
        if (segmentInfo.isTwoWay()) {
            return segmentInfo.getModifiedSecondWay();
        } else {
            return null;
        }
    }

    public SimulationElement getModifiedStation() {
        return (Station) stationInfo.getStation();
    }

    public SimulationElement getModifiedIntersection() {
        return (Intersection) intersectionInfo.getIntersection();
    }

    public SimulationElement getModifiedCircuit() {
        return (Circuit) circuitInfo.getModifiedTransit();
    }

    public SimulationElement getModifiedTransportNeed() {
        return (TransportNeed) transportNeedInfo.getModifiedTransit();
    }

    public boolean isAnimationParametersOpen() {
        return getContainedElement().containsValue(animationParameters);
    }

    public AnimatorImage getParametrisedAnimation() {
        return animationParameters.getParametrisedAnimation();
    }
}
