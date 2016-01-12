/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.networkelement;

import planeswalkers.simulatheure.data.units.TriangularDistribution;

/**
 * Link between station and intersection. Internal representation of a road.
 *
 * @author Antoine
 */
public class Segment extends NetworkElement {

    private final int origin;
    private final int end;
    private final TriangularDistribution transitTimeDistribution;

    /**
     *
     * @param name The name of the NetworkElement.
     * @param origin Id of the origin Node
     * @param end Id of the end Node
     * @param transitTimeDistribution
     */
    public Segment(String name, int origin, int end, TriangularDistribution transitTimeDistribution) {
        super(name);
        this.origin = origin;
        this.end = end;
        this.transitTimeDistribution = transitTimeDistribution;
    }

    public Segment(String name, int identifier, int origin, int end, TriangularDistribution transitTimeDistribution) {
        super(name, identifier);
        this.origin = origin;
        this.end = end;
        this.transitTimeDistribution = transitTimeDistribution;
    }

    public void resetTransitTime() {
        transitTimeDistribution.reset();
    }

    public int fetchTransitTime() {
        return transitTimeDistribution.fetchComputation();
    }

    public int getOrigin() {
        return origin;
    }

    public int getEnd() {
        return end;
    }

    public TriangularDistribution getTriangularDistribution() {
        return transitTimeDistribution;
    }
}
