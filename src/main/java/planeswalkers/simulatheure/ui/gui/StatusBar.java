/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import java.awt.Color;
import planeswalkers.simulatheure.data.units.Coordinates;
import planeswalkers.simulatheure.ui.components.Text;
import planeswalkers.simulatheure.ui.components.TextBox;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 *
 * @author Jean-Benoît
 */
public class StatusBar extends TextBox {

    static final double statusHeight = 24;

    /**
     * This creates a status bar in the bottom of a Displayable element. It's
     * default height is 30 pixels.
     *
     * @param screen the framing viewport of the element which the Status bar is
     * under
     */
    public StatusBar(Viewport screen) {
        super(StatusBar.class.getSimpleName(), screen.getX(), screen.getHeight() - statusHeight, new Viewport(screen.getWidth(), 0, -325, 0));
        setBackgroundColor(Color.DARK_GRAY);
        elementShape = new Viewport(screen.getX(), screen.getHeight() - statusHeight, screen.getWidth(), statusHeight);
        setFrameColor(new Color(0, 0, 0, 0.5f));
        initializeStatusText();

    }

    private void initializeStatusText() {
        addChild(new Text("Latitude"));
        addChild(new Text("Longitude"));
        addChild(new Text("ZoomRatio"));
        getChild("Longitude").setContent(" - ");
        getChild("Longitude").notEditable();
        getChild("Latitude").setContent(" - ");
        getChild("Latitude").notEditable();
        getChild("ZoomRatio").setContent("15.0");
        getChild("ZoomRatio").notEditable();
        setTextBounds();
    }

    public void updateCoordinates(Coordinates coordinates) {
        getContainedElement().get("Longitude").setContent(String.valueOf(coordinates.getLongitude()));
        getContainedElement().get("Latitude").setContent(String.valueOf(coordinates.getLatitude()));
    }

    public void updateZoomRatio(double zoomRatio) {
        getContainedElement().get("ZoomRatio").setContent(String.valueOf(zoomRatio));
    }

    @Override
    public void setRenderProperties(Viewport viewport) {
    }

}
