/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import planeswalkers.simulatheure.data.units.Coordinates;
import planeswalkers.simulatheure.ui.AssetManager;

/**
 *
 * @author Marc-Antoine Fortier
 */
public class IntersectionImage extends NodeImage {

    public IntersectionImage(String name, int uniqueIdentifier, Coordinates coordinates) {
        super(name, uniqueIdentifier, coordinates);
        elementShape = new Viewport(AssetManager.getInstance().getShape(IntersectionImage.class.getSimpleName()));
        diagram = AssetManager.getInstance().getSimulationComponent(IntersectionImage.class.getSimpleName());
    }

}
