/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.util.ArrayList;
import planeswalkers.simulatheure.data.networkelement.Segment;
import planeswalkers.simulatheure.data.transit.Circuit;
import planeswalkers.simulatheure.data.transit.CircuitFraction;
import planeswalkers.simulatheure.data.transit.Transit;
import planeswalkers.simulatheure.data.transit.TransportNeed;
import planeswalkers.simulatheure.data.units.TriangularDistribution;
import planeswalkers.simulatheure.ui.gui.SegmentInformationBox;

/**
 *
 * @author Jean-Benoit Harvey
 */
public class TransitEditionBox extends NetworkEditionBox {

    protected SegmentInformationBox pathAndDistribution;
    protected SegmentList takenPath;
    protected MobileElementInfo mobileInfo;
    protected TransitImage shownTransit;
    protected Integer uniqueIdentifier;
    private boolean isCircuit = false;

    public TransitEditionBox(TransitImage transit, double x, double y) {
        super(TransitEditionBox.class.getSimpleName(), x, y);
        boxWidth = 300;
        shownTransit = transit;
        uniqueIdentifier = shownTransit.getIdentifier();
        takenPath = new SegmentList(name);

        if (transit.getClass().getSimpleName().equals(CircuitImage.class.getSimpleName())) {
            CircuitImage circuitImage = (CircuitImage) transit;
            takenPath.loadCircuit(circuitImage.getSegments());
            isCircuit = true;
        } else {
            TransportNeedImage transportNeedImage = (TransportNeedImage) transit;
            takenPath.loadTransportNeed(transportNeedImage.getSegmentsOrganizedByCircuit());
        }
        mobileInfo = new MobileElementInfo(transit);
        pathAndDistribution = new SegmentInformationBox((SegmentImage) transit);
        addChild(pathAndDistribution);
        addChild(mobileInfo);
        addChild(takenPath);

        setBackgroundColor(INVISIBLE);
    }

    @Override
    public void setRenderProperties(Viewport viewport) {
        relativeX = viewport.getX();
        relativeY = viewport.getY();
        double childX = relativeX + horizontalPadding;
        double childY = relativeY + verticalPadding;
        double childWidth = boxWidth + horizontalPadding;
        double childHeigth = 0;
        for (Box box : getContainedElement().values()) {
            childHeigth = box.getBoxHeight() + verticalPadding;
            box.setRenderProperties(new Viewport(childX, childY, childWidth, childHeigth));
            childY += childHeigth + verticalPadding;
        }
        elementShape = new Viewport(relativeX, relativeY, childWidth + horizontalPadding, getBoxHeight() + childHeigth);
    }

    @Override
    public void close() {
        removeIfPresent(mobileInfo);
        removeIfPresent(pathAndDistribution);
        removeIfPresent(takenPath);
        super.close();
    }

    public Transit getModifiedTransit() {
        Transit modifiedTransit;
        Segment temporarySegment = (Segment) pathAndDistribution.getModifiedElement();
        String transitName = temporarySegment.getName();
        TriangularDistribution distribution = temporarySegment.getTriangularDistribution();
        int firstApparition = mobileInfo.getFirstApparition();
        if (isCircuit) {
            ArrayList<Integer> path = takenPath.getCircuitPath();
            int maximalBusNumber = mobileInfo.getMaximalCapacity();
            int busCapacity = mobileInfo.getBusCapacity();
            boolean loop = mobileInfo.isLoop();
            modifiedTransit = new Circuit(transitName, uniqueIdentifier, path, firstApparition, busCapacity, loop, maximalBusNumber, distribution);
        } else {
            ArrayList<CircuitFraction> circuitFractions = takenPath.getTransportNeedPath();
            modifiedTransit = new TransportNeed(transitName, uniqueIdentifier, circuitFractions, firstApparition, distribution);
        }
        return modifiedTransit;
    }
}
