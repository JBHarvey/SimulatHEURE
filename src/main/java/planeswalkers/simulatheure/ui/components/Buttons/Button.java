/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components.Buttons;

import java.awt.Color;
import java.awt.Graphics2D;
import planeswalkers.simulatheure.ui.components.Displayable;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 *
 * @author Jean-Benoît
 */
public abstract class Button extends Displayable {

    final double BUTTON_SIZE = 360;
    final String imageName;
    protected Color veilColor;

    /**
     *
     * @param name
     */
    public Button(String name) {
        super(name);
        imageName = name;
    }

    public Button(String name, String imageName) {
        super(name);
        this.imageName = imageName;
    }

    protected void paintVeil(Graphics2D graphics2D) {
        graphics2D.setColor(veilColor);
        graphics2D.fill(elementShape.getShape());
    }

    @Override
    public void focus() {
        veilColor = new Color(1f, 1f, 1f, 0.1f);
    }

    @Override
    public void unfocus() {
        veilColor = new Color(0f, 0f, 0f, 0f);

    }

    @Override
    public void press() {
        veilColor = new Color(0f, 0f, 0f, 0.1f);
    }

    @Override
    public void unpress() {
        veilColor = new Color(0f, 0f, 0f, 0f);
    }

    public void placeName(Viewport viewport) {

    }
}
