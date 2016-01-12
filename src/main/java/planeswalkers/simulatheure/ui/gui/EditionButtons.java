/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import java.awt.Color;
import planeswalkers.simulatheure.ui.components.Buttons.Button;
import planeswalkers.simulatheure.ui.components.RectangularBox;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 *
 * @author Jean-Benoît
 */
public class EditionButtons extends RectangularBox {

    private double buttonSize = 100;
    private double boxWidth = 0;

    /**
     * When creating such a box, consider the specific x and y positions:
     *
     * @param x The horizontal x position of the upper-right corner
     * @param y The vertical y position of the upper-right corner
     */
    public EditionButtons(double x, double y) {
        super(EditionButtons.class.getSimpleName(), x, y);
        setBackgroundColor(new Color(117, 163, 248, 160));
    }

    @Override
    public void addButton(Button button) {
        addChild(button);
        double originalX = relativeX + boxWidth;
        resize(new Viewport(originalX, 1, 1, 1));
    }

    @Override
    public void resize(Viewport r) {
        relativeX = r.getX();
        boxWidth = buttonSize * getContainedElement().size();
        super.resize(new Viewport(relativeX - boxWidth, relativeY, boxWidth, 5));
    }

    @Override
    public double getBoxWidth() {
        return buttonSize * getContainedElement().size();
    }

    public void setButtonSize(double buttonImageSize) {
        buttonSize = buttonImageSize;
        setButtonImageSize(buttonImageSize);
    }

}
