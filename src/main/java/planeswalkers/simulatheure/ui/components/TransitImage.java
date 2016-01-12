/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.ListIterator;
import planeswalkers.simulatheure.data.transit.Transit;

/**
 *
 * @author Marc-Antoine
 */
public abstract class TransitImage extends SegmentImage {

    protected ArrayList<PathImage> segments;
    protected Color transitColor;
    protected int firstApparitionTime;
    private float circuitIndicatorWidth;

    public TransitImage(Transit transit) {
        super(transit.getName(), transit.getIdentifier(), transit.getSpawnDistribution());
        firstApparitionTime = transit.getFirstSpawnTime();
    }

    public Color getColor() {
        return transitColor;
    }

    public void setTransitColor(Color transitColor) {
        this.transitColor = transitColor;
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        for (PathImage pathImage : segments) {
            SegmentImage segmentImage = pathImage.getSegmentImage();
            int rank = Integer.MAX_VALUE;
            int rankInv = Integer.MAX_VALUE;
            boolean found = false;
            ListIterator it = segmentImage.getTransitList().listIterator();
            while (it.hasNext() && !found) {
                int transitId = (Integer) it.next();

                if (transitId == this.getIdentifier()) {
                    rank = it.nextIndex() - 1;
                    found = true;
                }
            }
            if (!found) {
                rank = Integer.MAX_VALUE;
            } else {
                found = false;
            }
            it = segmentImage.getReverseTransitList().listIterator();
            while (it.hasNext() && !found) {
                int circuitId = (Integer) it.next();
                if (circuitId == this.getIdentifier()) {
                    rankInv = it.nextIndex() - 1;
                    found = true;
                }
            }
            if (!found) {
                rankInv = Integer.MAX_VALUE;
            }

            graphics2D.setColor(getColor());
            circuitIndicatorWidth = (float) (segmentImage.getOrigin().phiRatio(4.25));
            graphics2D.setStroke(new BasicStroke(circuitIndicatorWidth));
            Line2D line = new Line2D.Double();
            Line2D line2 = new Line2D.Double();
            if (rankInv != Integer.MAX_VALUE) {
                line = Viewport.getLineWithOffset(new Line2D.Double(segmentImage.placeNode(segmentImage.getEnd()), segmentImage.placeNode(segmentImage.getOrigin())),
                        (segmentImage.getOrigin().phiRatio(-1) / 4.0) + (double) (rankInv + 1) * (double) circuitIndicatorWidth - (double) circuitIndicatorWidth - 1);
            }
            if (rank != Integer.MAX_VALUE) {
                line2 = Viewport.getLineWithOffset(new Line2D.Double(segmentImage.placeNode(segmentImage.getOrigin()), segmentImage.placeNode(segmentImage.getEnd())),
                        (segmentImage.getOrigin().phiRatio(-1) / 4.0) + (double) (rank + 1) * (double) circuitIndicatorWidth - (double) circuitIndicatorWidth - 1);
            }
            if (this instanceof TransportNeedImage) {
                graphics2D.setColor(Color.LIGHT_GRAY.brighter());
                //graphics2D.draw(line);
                //graphics2D.draw(line2);
                float dash1[] = {2.0f * (float) (circuitIndicatorWidth)};
                graphics2D.setStroke(new BasicStroke(circuitIndicatorWidth, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        (float) circuitIndicatorWidth, dash1, 0.0f));
                graphics2D.setColor(getColor());
            }
            graphics2D.draw(line);
            graphics2D.draw(line2);
        }

    }

    @Override
    public void setRenderProperties(Viewport viewport) {
        elementShape = new Viewport(-1, -1, 0, 0);
    }

    public int getFirstApparitionTime() {
        return firstApparitionTime;
    }

    public void setSegments(ArrayList<PathImage> segments) {
        this.segments = segments;
    }

    public ArrayList<PathImage> getSegments() {
        return segments;
    }

}
