/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;

/**
 *
 * @author Marc-Antoine Fortier
 */
public abstract class Displayable extends JPanel {

    public static final Color INVISIBLE = new Color(0, 0, 0, 0);
    protected static final double PHI = (1 + Math.sqrt(5)) / 2;
    protected static final double PI = Math.PI;
    protected String name;
    protected Text nameLabel;
    protected Displayable parent;
    protected Viewport elementShape;
    protected Color displayColour;
    protected Color activeColour;
    protected double relativeX;
    protected double relativeY;
    protected boolean visible = true;
    protected boolean isNameVisible = false;
    boolean editable = false;

    public Displayable(String name) {

        this.setVisible(true);
        this.setFocusable(true);
        this.setIgnoreRepaint(true);
        this.name = name;
        unfocus();
    }

    /**
     * This method decorates paint to ensure the conservation of
     * graphicConfiguration
     *
     * @param graphics2D
     */
    public void Display(Graphics2D graphics2D) {

        saveGraphicsConfigurations(graphics2D);
        paint(graphics2D);
        restoreGraphicsConfigurations(graphics2D);
    }

    /**
     *
     * @param graphics2D
     */
    public abstract void paint(Graphics2D graphics2D);

    void displayName(Graphics2D graphics2D) {

    }

    /**
     * Will return the highest child of this Box. that is the child that is the
     * most nested in other boxes which all intersect or contain the Viewport
     * passed in parameters
     *
     * @param viewport The mouse position when the MouseEvent occurred, that the
     * highest child of this Box is in contact with
     * @return the child at the highest nested level.
     */
    public Displayable findHighestClick(Viewport viewport) {
        return this;
    }

    /**
     *
     * @param isVisible
     */
    public void setNameVisibility(boolean isVisible) {
        isNameVisible = isVisible;
    }

    /**
     *
     * @param viewport
     */
    public abstract void setRenderProperties(Viewport viewport);

    /**
     *
     * @return
     */
    @Override
    public Displayable getParent() {
        return parent;
    }

    /**
     *
     * @param r
     */
    public void resize(Viewport r) {

    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    public Viewport getElementShape() {
        return elementShape;
    }

    public boolean isEditable() {
        return editable;
    }

    public abstract void focus();

    public abstract void unfocus();

    public abstract void press();

    public abstract void unpress();

    private Font savedFont;
    private Color savedColor;
    private Stroke savedStroke;
    private AffineTransform savedAffineTransform;

    public void saveGraphicsConfigurations(Graphics2D graphics2D) {
        savedColor = graphics2D.getColor();
        savedFont = graphics2D.getFont();
        savedAffineTransform = graphics2D.getTransform();
        savedStroke = graphics2D.getStroke();
    }

    public void restoreGraphicsConfigurations(Graphics2D graphics2D) {
        graphics2D.setTransform(savedAffineTransform);
        graphics2D.setFont(savedFont);
        graphics2D.setStroke(savedStroke);
        graphics2D.setColor(savedColor);
    }

    /**
     * Generates a ratio of the width of the displayable and a specified power
     * of PHI
     *
     * @param power the power wanted
     * @return width / PHI ^[power]
     */
    public double phiRatio(double power) {
        return (elementShape.getWidth() / Math.pow(PHI, power));
    }

}
