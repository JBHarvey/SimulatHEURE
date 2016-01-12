/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.Color;
import java.awt.Graphics2D;
import planeswalkers.simulatheure.ui.components.Buttons.Button;
import planeswalkers.simulatheure.ui.components.Buttons.SquareButton;

/**
 *
 * @author Jean-Benoît
 */
public class RectangularBox extends ButtonBox<String, SquareButton> {

    protected int horizontalButtonDelta = 0;
    protected int verticalButtonDelta = 0;

    /**
     *
     * @param name
     * @param x
     * @param y
     */
    public RectangularBox(String name, double x, double y) {
        super(name, x, y);
        backgroundColour = new Color(50, 50, 50);
        frameColor = new Color(0, 0, 0, 0.5f);
        setOpaque(true);
    }

    /**
     *
     * @param name
     */
    @Override
    public void addButton(Button button) {
        addChild(button);
        resize(new Viewport(0, 0, 1, 1));
    }

    /**
     *
     * @param r
     */
    @Override
    public void resize(planeswalkers.simulatheure.ui.components.Viewport r) {
        setRenderProperties(r);
        setButtonBounds();
    }

    @Override
    void paintBackground(Graphics2D graphics2D) {
        graphics2D.setColor(frameColor);
        graphics2D.fill(buttonGuide.getShape());
        graphics2D.setColor(backgroundColour);
        graphics2D.fill(elementShape.getShape());
    }

    /**
     *
     * @param viewport
     */
    @Override
    public void setRenderProperties(planeswalkers.simulatheure.ui.components.Viewport viewport) {

        double width = viewport.getWidth();
        double height = viewport.getHeight();
        double minimalSize = buttonImageSize * getContainedElement().size();
        if (width * height * buttonImageSize < minimalSize || width < buttonImageSize || height < buttonImageSize) {
            if (width < buttonImageSize) {
                width = buttonImageSize;
            }
            width = Math.floor(width / buttonImageSize) * buttonImageSize;
            height = minimalSize / width * buttonImageSize;
        }
        if (height % buttonImageSize != 0) {
            height += (buttonImageSize - (height % buttonImageSize));
        }
        buttonGuide = new Viewport(relativeX - 5, relativeY - 5, (int) width + 10, (int) height + 10);
        elementShape = new Viewport(relativeX, relativeY, width, height);
    }

    /**
     *
     */
    @Override
    void setButtonBounds() {
        int buttonX = horizontalButtonDelta;
        int buttonY = verticalButtonDelta;
        double width = elementShape.getBounds().width;

        for (Button b : getContainedElement().values()) {
            b.setRenderProperties(new Viewport(buttonX + relativeX, buttonY + relativeY, buttonImageSize, buttonImageSize));
            if (buttonX + 2 * buttonImageSize <= width) {
                buttonX += buttonImageSize;
            } else {
                buttonX = 0;
                buttonY += buttonImageSize;
            }
        }
    }

}
