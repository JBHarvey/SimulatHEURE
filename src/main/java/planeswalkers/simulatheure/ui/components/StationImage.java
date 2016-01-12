/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import planeswalkers.simulatheure.animation.Passenger;
import planeswalkers.simulatheure.data.units.Coordinates;
import planeswalkers.simulatheure.ui.AssetManager;

/**
 *
 * @author Marc-Antoine Fortier
 */
public class StationImage extends NodeImage {
    
     private int numberOfpassenger;

    public StationImage(String name, int uniqueIdentifier, Coordinates coordinates) {
        super(name, uniqueIdentifier, coordinates);
        elementShape = new Viewport(AssetManager.getInstance().getShape(StationImage.class.getSimpleName()));
        diagram = AssetManager.getInstance().getSimulationComponent(StationImage.class.getSimpleName());
        numberOfpassenger = 0;
    }

    @Override
    protected void initNameLabel() {
        nameLabel = new Text("");
        nameLabel.setContent(displayName + " (" + numberOfpassenger + ")");
        nameLabel.setContentColor(Color.BLACK);
        nameLabel.editable = false;
        nameLabel.setSeparator("");
    }

    @Override
    void displayName(Graphics2D graphics2D) {
        nameLabel.setContent(displayName + " (" + numberOfpassenger + ")");
        super.displayName(graphics2D);
    }
    
    public int getNumberOfpassenger() {
        return numberOfpassenger;
    }

    public void setNumberOfpassenger(int numberOfpassenger) {
        this.numberOfpassenger = numberOfpassenger;
    }
}
