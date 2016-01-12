/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.Graphics2D;

/**
 *
 * @author Jean-Benoit
 */
public abstract class TextBox extends Box<String, Text> {

    protected static final Viewport defaultTextOffsets = new Viewport(20, 0, 0, 20);
    protected double horizontalOffset;
    protected double verticalOffset;
    protected double horizontalDelta;
    protected double verticalDelta;

    /**
     *
     * @param name name of the textBox
     * @param x relativeX position of the element
     * @param y relativeY position of the element
     * @param offsets where x = relativeX position of the first text element, y
     * = relativeY position of the first text element width = horizontal space
     * between each text element height = vertical space between each text
     * element
     */
    public TextBox(String name, double x, double y, Viewport offsets) {
        super(name, x, y);

        horizontalOffset = offsets.getX();
        verticalOffset = offsets.getY();
        horizontalDelta = offsets.getWidth();
        verticalDelta = offsets.getHeight();
    }

    @Override
    public void paint(Graphics2D graphics2D) {

        saveGraphicsConfigurations(graphics2D);
        graphics2D.setColor(frameColor);
        graphics2D.draw(elementShape.getShape());
        graphics2D.setColor(backgroundColour);
        graphics2D.fill(elementShape.getShape());
        for (Text t : getContainedElement().values()) {
            t.paint(graphics2D);
        }
        restoreGraphicsConfigurations(graphics2D);
    }

    @Override
    public double getBoxHeight() {
        return (verticalDelta * getContainedElement().size());
    }

    @Override
    public void setRenderProperties(Viewport viewport) {
        relativeX = viewport.getX();
        relativeY = viewport.getY();
        elementShape = viewport;
        setTextBounds();
    }

    protected void setTextBounds() {
        horizontalOffset += elementShape.getX();
        verticalOffset += elementShape.getY() + 20;
        for (Text t : getContainedElement().values()) {
            horizontalOffset += horizontalDelta;
            t.setRenderProperties(new Viewport(horizontalOffset, verticalOffset));
            verticalOffset += verticalDelta;
        }
    }

    public void setTextBoxForPaint(Graphics2D gaphics2D) {
        for (Text t : getContainedElement().values()) {
            t.updateTextBounds(gaphics2D);
        }
    }

    public void setVerticalDelta(double verticalDelta) {
        this.verticalDelta = verticalDelta;
    }

    public void setHorizontalDelta(double horizontalDelta) {
        this.horizontalDelta = horizontalDelta;
    }

}
