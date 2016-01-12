/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.networkelement;

import planeswalkers.simulatheure.data.units.Coordinates;

/**
 * Internal representation of a bus station
 *
 * @author Jean-Benoît
 */
public class Station extends Node {
    
    /**
     * Station constructor
     *
     * @param name the name of the station
     * @param coordinates the present coordinates of the station.
     */
    public Station(String name, Coordinates coordinates) {
        super(name, coordinates);
    }

    public Station(String name, int identifier, Coordinates coordinates) {
        super(name, identifier, coordinates);
    }
}
