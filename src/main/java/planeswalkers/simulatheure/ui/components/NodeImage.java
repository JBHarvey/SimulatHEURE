/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import planeswalkers.simulatheure.data.units.Coordinates;

/**
 * The UI representation of an node (station and intersection). Methods to paint
 * the component and to listen on it are defined.
 *
 * @author Marc-Antoine Fortier
 */
public abstract class NodeImage extends SimulationImage {

    private final ArrayList<SegmentImage> linkedSegments;
    private Coordinates coordinates;
    private boolean lockForCreation;

    /**
     * Constructs NodeImage object.
     *
     * @param name The node's name. (ex. "Station1").
     * @param uniqueIdentifier
     * @param coordinates
     */
    public NodeImage(String name, int uniqueIdentifier, Coordinates coordinates) {
        super(name, uniqueIdentifier);
        linkedSegments = new ArrayList<>();
        this.coordinates = coordinates;
    }

    @Override
    protected AffineTransform createTransform() {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(elementShape.getX(), elementShape.getY());
        affineTransform.scale(elementShape.getWidth() / diagram.getWidth(), elementShape.getHeight() / diagram.getHeight());
        return affineTransform;
    }

    /**
     * Sets the rendering properties of an element, i.e. his viewport (shape and
     * position).
     *
     * @param viewport The viewport of the element to display. The width and
     * height aren't sets according to the element's representation shape but it
     * should contain the proper scaling ratio to apply onto the SVG shape.
     */
    @Override
    public void setRenderProperties(Viewport viewport) {
        elementShape = new Viewport(new Ellipse2D.Double(viewport.getX(), viewport.getY(), diagram.getWidth() * viewport.getWidth(), diagram.getHeight() * viewport.getHeight()));
    }

    public Point getXY() {
        return new Point((int) elementShape.getX(), (int) elementShape.getY());
    }

    public Point getCenterXY() {
        return elementShape.getCenterXY();
    }

    @Override
    protected void renderBackground(Graphics2D graphics2D) {
        saveGraphicsConfigurations(graphics2D);
        graphics2D.setColor(selectionColor);
        graphics2D.fill(new Ellipse2D.Double(elementShape.getX() - (elementShape.getWidth() / Math.pow(PHI, 2)) / 2,
                elementShape.getY() - (elementShape.getHeight() / Math.pow(PHI, 2)) / 2,
                elementShape.getWidth() + (elementShape.getWidth() / Math.pow(PHI, 2)),
                elementShape.getHeight() + (elementShape.getHeight() / Math.pow(PHI, 2))));
        restoreGraphicsConfigurations(graphics2D);
    }

    @Override
    void displayName(Graphics2D graphics2D) {
        Viewport labelPosition = new Viewport(elementShape.getX() + elementShape.getWidth() / 2, elementShape.getY() - 20, 1, 1);
        nameLabel.setRenderProperties(labelPosition);
        nameLabel.updateTextBounds(graphics2D);
        nameLabel.centerHorizontally();
        nameLabel.setContentBoxColor(new Color(255, 255, 255, 150));
        nameLabel.paint(graphics2D);
    }

    public void addLinkedSegment(SegmentImage segmentImage) {
        linkedSegments.add(segmentImage);
    }

    public ArrayList<SegmentImage> getLinkedSegments() {
        return linkedSegments;
    }

    public void removeLinkedSegment(SegmentImage segmentImage) {
        linkedSegments.remove(segmentImage);
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    /*public BufferedImage getImage() {
     BufferedImage bufferedImage = new BufferedImage(35, 35, BufferedImage.TYPE_INT_ARGB);
     this.paint((Graphics2D) bufferedImage.getGraphics());
     return bufferedImage;
     }*/
    public void moveOf(Coordinates delta) {
        coordinates = coordinates.getDelta(delta);
    }

    @Override
    public void selectForCreation() {
        if (lockForCreation) {
            selectionColor = Color.ORANGE;
        } else {
            selectionColor = Color.YELLOW;
        }
    }

    @Override
    public void unselect() {
        if (!lockForCreation) {
            selectionColor = INVISIBLE;
        }
    }

    public void lockForCreation() {
        lockForCreation = true;
        selectionColor = Color.RED;
    }

    public void unlockAndUnselect() {
        lockForCreation = false;
        selectionColor = INVISIBLE;

    }

}
