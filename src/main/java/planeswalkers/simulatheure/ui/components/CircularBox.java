/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import planeswalkers.simulatheure.ui.components.Buttons.Button;
import planeswalkers.simulatheure.ui.components.Buttons.CircularButton;

/**
 *
 * @author Jean-Benoit
 */
public class CircularBox extends ButtonBox<String, CircularButton> {

    /**
     *
     */
    protected Color buttonColor;
    private final double buttonRadius = buttonImageSize / 2;
    private double displayRadius;
    private double centerX, centerY;
    private double halfSide;
    protected double phaseChange = 0;
    protected double angleContraction = 1;

    /**
     *
     * @param name
     */
    public CircularBox(String name) {
        super(name, 0, 0);
        backgroundColour = new Color(53, 72, 163);
        frameColor = new Color(117, 163, 248);
        setOpaque(false);
    }

    /**
     *
     * @param name
     */
    @Override
    public void addButton(Button button) {
        addChild(button);
        int size = getContainedElement().size();
        if (size < 4) {
            displayRadius = PHI * (buttonRadius * (size - 1));
        }
    }

    @Override
    void paintBackground(Graphics2D graphics2D) {

        float strokeWidth = (float) (buttonRadius / PHI);
        graphics2D.setStroke(new BasicStroke(strokeWidth));
        graphics2D.setColor(frameColor);
        int guideX = (int) (relativeX + strokeWidth + buttonRadius / 2);
        int guideY = (int) (relativeY + strokeWidth + buttonRadius / 2);
        graphics2D.drawOval(guideX, guideY, (int) (2 * displayRadius), (int) (2 * displayRadius));

    }

    /**
     * The given X and Y coordinates of the viewport are the center of the
     * circular menu
     *
     * @param viewport
     */
    @Override
    public void setRenderProperties(planeswalkers.simulatheure.ui.components.Viewport viewport) {
        centerX = viewport.getX();
        centerY = viewport.getY();
        halfSide = displayRadius + buttonRadius;
        relativeX = centerX - halfSide;
        relativeY = centerY - halfSide;
        elementShape = new Viewport(relativeX, relativeY, 2 * halfSide, 2 * halfSide);
        setButtonBounds();
    }

    @Override
    void setButtonBounds() {

        double theta = 2 * PI / getContainedElement().size();
        double currentTheta = phaseChange;
        double buttonX, buttonY, textLabelX, textLabelY;
        for (Button b : getContainedElement().values()) {
            buttonX = centerX + (Math.cos(currentTheta) * displayRadius) - buttonRadius;
            buttonY = centerY + (Math.sin(currentTheta) * displayRadius) - buttonRadius;
            textLabelX = centerX + (Math.pow(PHI, PHI) * Math.cos(currentTheta) * displayRadius) - buttonRadius;
            textLabelY = centerY + (Math.pow(PHI, PHI) * Math.sin(currentTheta) * displayRadius) - buttonRadius;
            b.setRenderProperties(new Viewport(buttonX, buttonY, buttonImageSize, buttonImageSize));
            b.placeName(new Viewport(textLabelX - buttonX, textLabelY - buttonY));
            currentTheta += theta * angleContraction;
        }
    }

    @Override
    public double getBoxHeight() {
        return elementShape.getHeight();
    }
}
