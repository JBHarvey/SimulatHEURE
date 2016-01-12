/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.Color;
import java.awt.Graphics2D;
import planeswalkers.simulatheure.data.SimulationElement;

/**
 *
 * @author Jean-Benoit
 * @param <S>
 */
public abstract class NetworkInformationBox extends Box<String, TextBox> {

    protected double boxWidth = 350;
    protected SimulationImage showedElement;
    protected Integer elementID;

    public NetworkInformationBox(String name, SimulationImage element) {
        super(name, 0, 0);
        this.showedElement = element;
        elementID = element.getIdentifier();
        setBackgroundColor(new Color(117, 163, 248));
        setFrameColor(new Color(53, 72, 163, 127));
        horizontalPadding = 5;
        verticalPadding = 5;
    }

    protected void setTextBoxesBounds() {
    }

    public void setVerticalPadding(double verticalPadding) {
        this.verticalPadding = verticalPadding;
    }

    public void setHorizontalPadding(double horizontalPadding) {
        this.horizontalPadding = horizontalPadding;
    }

    public void setBoxWidth(double boxWidth) {
        this.boxWidth = boxWidth;
    }

    @Override
    public void setRenderProperties(Viewport viewport) {
        relativeX = viewport.getX();
        relativeY = viewport.getY();

        double childX = relativeX + horizontalPadding;
        double childY = relativeY + verticalPadding;
        double childWidth = boxWidth - 2 * horizontalPadding;
        double childHeigth;
        for (TextBox tb : getContainedElement().values()) {
            childHeigth = tb.getBoxHeight() + verticalPadding;
            tb.setRenderProperties(new Viewport(childX, childY, childWidth, childHeigth));
            childY += childHeigth + (verticalPadding * 2);
        }
        elementShape = new Viewport(relativeX, relativeY, boxWidth, childY + verticalPadding);
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        saveGraphicsConfigurations(graphics2D);
        for (TextBox tb : getContainedElement().values()) {
            tb.paint(graphics2D);
        }
        restoreGraphicsConfigurations(graphics2D);
    }

    @Override
    public double getBoxHeight() {
        double boxHeight = 2 * verticalPadding;
        for (TextBox box : getContainedElement().values()) {
            boxHeight += box.getBoxHeight();
        }
        return boxHeight;
    }

    public abstract SimulationElement getModifiedElement();
}
