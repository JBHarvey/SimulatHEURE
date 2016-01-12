/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.networkelement;

import planeswalkers.simulatheure.data.units.Coordinates;

/**
 *
 * @author Jean-Benoît
 */
public abstract class Node extends NetworkElement {

    /**
     * coordinates the node location.
     *
     * The coordinates is unique amongst all nodes.
     */
    protected final Coordinates coordinates;

    /**
     * The Node abstract constructor
     *
     * @param name The name of the node
     * @param coordinates The position of the node
     */
    public Node(String name, Coordinates coordinates) {
        super(name);
        this.coordinates = coordinates;
    }

    public Node(String name, int identifier, Coordinates coordinates) {
        super(name, identifier);
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return this.coordinates;
    }
}
