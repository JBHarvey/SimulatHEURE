/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import com.kitfox.svg.SVGElement;
import com.kitfox.svg.SVGElementException;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.animation.AnimationElement;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.logging.Level;
import java.util.logging.Logger;
import planeswalkers.simulatheure.animation.Bus;
import planeswalkers.simulatheure.ui.AssetManager;

/**
 *
 * @author Marc-Antoine Fortier
 */
public class BusImage extends EntityImage {

    private double progression;
    private SVGElement colorElement;
    private int passengersCount;
    private SegmentImage currentSegment;
    private String circuitName;
    private Color color;
    private boolean reverse;

    public BusImage(String name, int uniqueIdentifier) {
        super(Bus.class.getSimpleName(), uniqueIdentifier);
        circuitName = name;
        progression = 0.0;
        passengersCount = 0;
        diagram = AssetManager.getInstance().getUIToolComponent(BusImage.class.getSimpleName());
        colorElement = diagram.getRoot().getChild("path8");
        elementShape = new Viewport();
        reverse = false;
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        super.paint(graphics2D);
        AffineTransform savedTransform = graphics2D.getTransform();
        AffineTransform transform = new AffineTransform();
        transform.translate(elementShape.getX() + Math.cos(currentSegment.getElementShape().getAngle(currentSegment.getOrigin().getXY(), currentSegment.getEnd().getXY())) * elementShape.getHeight(),
                elementShape.getY() + Math.sin(currentSegment.getElementShape().getAngle(currentSegment.getOrigin().getXY(), currentSegment.getEnd().getXY())) * elementShape.getHeight());
        transform.rotate(currentSegment.getElementShape().getAngle(currentSegment.getOrigin().getXY(), currentSegment.getEnd().getXY()));
        if (reverse) {
            transform.rotate(Math.PI);
        }
        transform.translate(-elementShape.getWidth(), -1.5 * elementShape.getWidth());
        graphics2D.transform(transform);
        graphics2D.setColor(color.brighter());
        graphics2D.fillRoundRect(0, 0, (int) (elementShape.getWidth() * 0.75), (int) (elementShape.getWidth() * 0.75), (int) (elementShape.getWidth() * 0.75 / 4.0), (int) (elementShape.getWidth() * 0.75 / 4.0));
        int[] xs = {(int) (0.2 * elementShape.getWidth() * 0.75), (int) (elementShape.getWidth() * 0.75 - 0.2 * elementShape.getWidth()), (int) (elementShape.getWidth() * 0.75 * 0.50)};
        int[] ys = {(int) (elementShape.getWidth() * 0.74), (int) (elementShape.getWidth() * 0.74), (int) (elementShape.getWidth())};
        Polygon triangle = new Polygon(xs, ys, 3);
        graphics2D.fillPolygon(triangle);
        graphics2D.setColor(getContrastColor(color.brighter()));
        graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, (int) (elementShape.getWidth() * 0.6)));
        graphics2D.setTransform(savedTransform);
        AffineTransform transform2 = new AffineTransform();
        transform2.translate(elementShape.getX() + Math.cos(currentSegment.getElementShape().getAngle(currentSegment.getOrigin().getXY(), currentSegment.getEnd().getXY())) * elementShape.getHeight(),
                elementShape.getY() + Math.sin(currentSegment.getElementShape().getAngle(currentSegment.getOrigin().getXY(), currentSegment.getEnd().getXY())) * elementShape.getHeight());
        transform2.rotate(currentSegment.getElementShape().getAngle(currentSegment.getOrigin().getXY(), currentSegment.getEnd().getXY()));
        if (reverse) {
            transform2.rotate(Math.PI);
        }
        transform2.translate(-7.8 * elementShape.getWidth() / 12.0, -1.1 * elementShape.getWidth());
        transform2.rotate(-currentSegment.getElementShape().getAngle(currentSegment.getOrigin().getXY(), currentSegment.getEnd().getXY()));
        if (String.valueOf(passengersCount).length() == 1) {
            transform2.translate(-graphics2D.getFontMetrics().charWidth(48) / 2, graphics2D.getFontMetrics().getHeight() / 3);
        } else {
            transform2.translate(-graphics2D.getFontMetrics().charWidth(48), graphics2D.getFontMetrics().getHeight() / 3);
        }
        graphics2D.transform(transform2);
        graphics2D.drawString(String.valueOf(passengersCount), 0, 0);
        graphics2D.setTransform(savedTransform);
    }

    @Override
    protected AffineTransform createTransform() {
        AffineTransform transform = new AffineTransform();
        transform.translate(elementShape.getX() + Math.cos(currentSegment.getElementShape().getAngle(currentSegment.getOrigin().getXY(), currentSegment.getEnd().getXY())) * elementShape.getHeight(),
                elementShape.getY() + Math.sin(currentSegment.getElementShape().getAngle(currentSegment.getOrigin().getXY(), currentSegment.getEnd().getXY())) * elementShape.getHeight());
        transform.scale((elementShape.getWidth() / (diagram.getWidth() * 0.84)), (elementShape.getHeight() / (diagram.getHeight() * 0.84)));
        transform.rotate(currentSegment.getElementShape().getAngle(currentSegment.getOrigin().getXY(), currentSegment.getEnd().getXY()) - Math.PI);
        if (reverse) {
            transform.rotate(Math.PI);
        }
        transform.rotate(Math.PI, diagram.getWidth() / 2.0, diagram.getHeight() / 2.0);
        changeBusColor();
        return transform;
    }

    private void changeBusColor() {
        try {
            String colorName = "#" + Integer.toHexString(color.getRGB()).substring(2);
            colorElement.setAttribute("fill", AnimationElement.AT_CSS, colorName);
            colorElement.updateTime(0f);
        } catch (SVGElementException ex) {
            Logger.getLogger(BusImage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SVGException ex) {
            Logger.getLogger(BusImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void renderBackground(Graphics2D graphics2D) {
    }

    public void setRenderProperties(double progression, int newCount, SegmentImage currentSegment, boolean reverse) {
        this.reverse = reverse;
        this.currentSegment = currentSegment;
        this.progression = progression;
        passengersCount = newCount;
        placeBusOnSegment();
    }

    @Override
    public void setRenderProperties() {
        placeBusOnSegment();
    }

    private void placeBusOnSegment() {
        if (currentSegment != null) {
            Point position = currentSegment.getPositionFromProgression(progression);
            int trackWidth = currentSegment.getTrackWidth();
            elementShape = new Viewport(position.getX(), position.getY(), (diagram.getWidth() / diagram.getHeight()) * (double) trackWidth, (double) trackWidth);
        }
    }

    @Override
    public void setRenderProperties(Viewport viewport) {
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public static Color getContrastColor(Color color) {
        double y = (299.0 * (double) color.getRed() + 587.0 * (double) color.getGreen() + 114.0 * (double) color.getBlue()) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }

}
