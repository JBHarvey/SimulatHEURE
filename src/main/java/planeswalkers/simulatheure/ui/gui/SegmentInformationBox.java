/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import planeswalkers.simulatheure.data.SimulationElement;
import planeswalkers.simulatheure.data.networkelement.Segment;
import planeswalkers.simulatheure.ui.components.NetworkInformationBox;
import planeswalkers.simulatheure.ui.components.SegmentImage;
import planeswalkers.simulatheure.ui.components.TriangularDistributionBox;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 *
 * @author Jean-Benoît
 */
public class SegmentInformationBox extends NetworkInformationBox {

    protected PathBasics basics;
    protected PathBasics secondWayBasic;
    protected TriangularDistributionBox distribution;
    protected TriangularDistributionBox secondWayDistribution;
    protected SegmentImage shownSegment;
    private boolean twoWay = false;

    public SegmentInformationBox(SegmentImage segment) {
        super(SegmentInformationBox.class.getSimpleName(), segment);
        elementShape = new Viewport(0, 0, boxWidth, 0);
        relativeX = elementShape.getX();
        relativeY = elementShape.getY();
        shownSegment = segment;
        basics = new PathBasics(shownSegment.getDisplayName());
        distribution = new TriangularDistributionBox(Segment.class.getSimpleName());
        addChild(basics);
        addChild(distribution);
        basics.open(segment);
        distribution.open(segment.getTriangularDistribution(segment.getIdentifier()));
        if (segment.isTwoWay()) {
            twoWay = true;
            secondWayBasic = new PathBasics(shownSegment.getDisplayName() + "SecondWay");
            secondWayDistribution = new TriangularDistributionBox(Segment.class.getSimpleName() + "SecondWay");
            addChild(secondWayBasic);
            addChild(secondWayDistribution);
            secondWayBasic.openSecondWay(segment);
            secondWayDistribution.open(segment.getTriangularDistribution(segment.getSecondWayId()));
        }
    }

    @Override
    public SimulationElement getModifiedElement() {
        SegmentImage segment = (SegmentImage) showedElement;
        int origin = segment.getOrigin().getIdentifier();
        int end = segment.getEnd().getIdentifier();
        return new Segment(basics.getPathName(), elementID, origin, end, distribution.getDistribution());
    }

    public boolean isTwoWay() {
        return twoWay;
    }

    public Segment getModifiedSecondWay() {
        SegmentImage segment = (SegmentImage) showedElement;
        int origin = segment.getEnd().getIdentifier();
        int end = segment.getOrigin().getIdentifier();
        int secondWayId = segment.getSecondWayId();
        return new Segment(basics.getPathName(), secondWayId, origin, end, secondWayDistribution.getDistribution());
    }

}
