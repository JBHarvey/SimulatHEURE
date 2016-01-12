/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import planeswalkers.simulatheure.ui.components.Displayable;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 *
 * @author Jean-Benoît
 */
public class ProgressBar extends Displayable {

    private final Viewport progressBackground;
    private Viewport progressDone;
    private double length, start, progression = 0;
    private final Color backColor = new Color(242, 242, 242);
    private final Color progressionColor = new Color(0, 124, 216);

    public ProgressBar(String name, Viewport backgroundBar) {
        super(name);
        progressBackground = backgroundBar;
        elementShape = progressBackground;
        setProgressionValue(0);
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        saveGraphicsConfigurations(graphics2D);
        graphics2D.setColor(backColor);
        graphics2D.fill(progressBackground.getShape());
        graphics2D.setColor(progressionColor);
        graphics2D.fill(progressDone.getShape());
        restoreGraphicsConfigurations(graphics2D);
    }

    public void setBorders(double start, double end) {
        length = end - start;
        this.start = start;
    }

    public final void setProgressionValue(double current) {
        progression = (current - start) / length;
        double x = progressBackground.getX() + 2;
        double y = progressBackground.getY() + 2;
        double width = (progressBackground.getWidth() - 4) * progression;
        double height = progressBackground.getHeight() - 4;
        progressDone = new Viewport(x, y, width, height);
    }

    public double getProgressionFromClickAt(double absoluteX) {
        double minimalX = progressBackground.getX() + 2;
        double maximalX = progressBackground.getX() + progressBackground.getWidth() - 2;
        double range = maximalX - minimalX;
        double clickedX = absoluteX - minimalX;
        double clickedProgress = clickedX / range;
        double clickedValue = clickedProgress * length + start;
        return clickedValue;
    }

    @Override
    public void setRenderProperties(Viewport viewport) {
    }

    @Override
    public void focus() {
    }

    @Override
    public void unfocus() {
    }

    @Override
    public void press() {
    }

    @Override
    public void unpress() {
    }

    @Override
    public Displayable findHighestClick(Viewport viewport) {
        if (progressBackground.intersects(viewport.getBounds2D())) {
            return this;
        }
        return null;
    }

}
