/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.networkelement;

import planeswalkers.simulatheure.data.units.Coordinates;

/**
 * Internal representation of an intersection.
 *
 * @author Jean-Benoît
 */
public class Intersection extends Node {

    /**
     * Intersection constructor
     *
     * @param name the name of the intersection
     * @param coordinates the present coordinates of the intersection.
     */
    public Intersection(String name, Coordinates coordinates) {
        super(name, coordinates);
    }
    
    public Intersection(String name, int identifier, Coordinates coordinates) {
        super(name, identifier, coordinates);
    }
    
}
