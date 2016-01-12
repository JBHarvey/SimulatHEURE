/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

/**
 *
 * @author Marc-Antoine Fortier
 */
public abstract class EntityImage extends SimulationImage {

    public EntityImage(String name, int uniqueIdentifier) {
        super(name, uniqueIdentifier);
    }

    public abstract void setRenderProperties();
}
