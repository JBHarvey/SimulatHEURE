/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.networkelement;

import java.io.Serializable;
import planeswalkers.simulatheure.data.SimulationElement;

/**
 * @author Jean-Benoît
 */
public abstract class NetworkElement extends SimulationElement{

    public NetworkElement(String name) {
        super(name);
    }

    public NetworkElement(String name, int identifier) {
        super(name, identifier);
    }
}
