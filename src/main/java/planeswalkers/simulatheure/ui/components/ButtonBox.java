/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.ListIterator;
import planeswalkers.simulatheure.ui.components.Buttons.Button;

/**
 *
 * @author Jean-Benoît
 * @param <S>
 * @param <B>
 *
 */
public abstract class ButtonBox<S, B extends Button> extends Box<String, Button> {

    protected int buttonImageSize = 80;

    Viewport buttonGuide;

    /**
     *
     * @param name
     * @param x
     * @param y
     */
    public ButtonBox(String name, double x, double y) {
        super(name, x, y);
    }

    /**
     *
     * @param button
     */
    public abstract void addButton(Button button);

    /**
     *
     * @param graphics2D
     */
    @Override
    public void paint(Graphics2D graphics2D) {
        paintBackground(graphics2D);
        paintButtons(graphics2D);
    }

    abstract void paintBackground(Graphics2D graphics2D);

    void paintButtons(Graphics2D graphics2D) {

        AffineTransform savedTransform = graphics2D.getTransform();
        int size = getContainedElement().size();
        ListIterator<Button> iter = new ArrayList<>(getContainedElement().values()).listIterator(size);
        while (iter.hasPrevious()) {
            Button b = iter.previous();
            graphics2D.setColor(backgroundColour);
            b.paint(graphics2D);
        }

        graphics2D.setTransform(savedTransform);

    }

    abstract void setButtonBounds();

    public void setButtonGuide(Viewport buttonGuide) {
        this.buttonGuide = buttonGuide;
    }

    /**
     *
     * @param viewport
     */
    @Override
    public void setRenderProperties(planeswalkers.simulatheure.ui.components.Viewport viewport) {

    }

    public void setButtonImageSize(double buttonImageSize) {
        this.buttonImageSize = (int) (0.8 * buttonImageSize);
    }
}
