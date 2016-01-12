/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import planeswalkers.simulatheure.data.units.TriangularDistribution;

/**
 *
 * @author Marc-Antoine Fortier
 */
public class SegmentImage extends SimulationImage {

    private NodeImage origin;
    private NodeImage end;
    private boolean isTwoWay;
    private TriangularDistribution triangularDistribution;
    private TriangularDistribution secondWayDistribution;
    private ArrayList<Integer> transitList;
    private ArrayList<Integer> reverseTransitList;
    private int secondWayId;
    private int transitTime;
    private int secondWaytransitTime;
    private boolean lockForCreation;

    /**
     *
     * @param name
     * @param uniqueIdentifier
     * @param triangularDistribution
     */
    public SegmentImage(String name, int uniqueIdentifier, TriangularDistribution triangularDistribution) {
        super(name, uniqueIdentifier);
        this.triangularDistribution = triangularDistribution;
        isTwoWay = false;
        secondWayId = Integer.MIN_VALUE;
        transitList = new ArrayList<>();
        reverseTransitList = new ArrayList<>();
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        renderShape(graphics2D, selectionColor, origin.phiRatio(1));
        renderShape(graphics2D, Color.GRAY, 0);
        graphics2D.setColor(Color.YELLOW);
        Point pointOrigin = placeNode(origin);
        Point pointEnd = placeNode(end);
        float dash1[] = {(float) (origin.phiRatio(3))};
        BasicStroke dashed
                = new BasicStroke((float) (origin.phiRatio(6)),
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        (float) (origin.phiRatio(3)), dash1, 0.0f);
        graphics2D.setStroke(dashed);
        graphics2D.drawLine((int) pointOrigin.getX(), (int) pointOrigin.getY(), (int) pointEnd.getX(), (int) pointEnd.getY());

        /**
         * DISPLAY ORIENTATION
         */
        Point arrowStart = getPositionFromProgression(1.0 / 3.0);
        Point arrowEnd = getPositionFromProgression(2.0 / 3.0);
        double arrowLength = Point.distance(arrowStart.getX(), arrowStart.getY(), arrowEnd.getX(), arrowEnd.getY());
        double arrowSideLength = Math.sqrt(Math.pow((5.0 / 12.0) * arrowLength, 2) + Math.pow(getTrackWidth(), 2));
        Point arrowSideProjectionPoint = getPositionFromProgression(1.0 - (arrowSideLength / arrowLength));
        double arrowSideAngle = Math.atan2(getTrackWidth(), arrowSideLength);
        graphics2D.setColor(Color.ORANGE);
        graphics2D.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics2D.drawLine((int) arrowStart.getX(), (int) arrowStart.getY(), (int) arrowEnd.getX(), (int) arrowEnd.getY());
        Line2D line = new Line2D.Double((Point2D) arrowEnd, (Point2D) arrowSideProjectionPoint);
        AffineTransform transformLeft = new AffineTransform();
        if (isTwoWay) {
            transformLeft.translate(arrowStart.getX() - arrowEnd.getX(), arrowStart.getY() - arrowEnd.getY());
            transformLeft.rotate(Math.PI - arrowSideAngle, line.getX1(), line.getY1());
        } else {
            transformLeft.rotate(arrowSideAngle, line.getX1(), line.getY1());
        }
        AffineTransform transformRight = new AffineTransform();
        transformRight.rotate(-arrowSideAngle, line.getX1(), line.getY1());
        Shape leftSide = transformLeft.createTransformedShape(line);
        Shape rightSide = transformRight.createTransformedShape(line);
        graphics2D.draw(leftSide);
        graphics2D.draw(rightSide);
        displayName(graphics2D);
    }

    private void renderShape(Graphics2D graphics2D, Color colour, double padding) {
        generateSegmentShape(padding);
        graphics2D.setColor(colour);
        graphics2D.fill(elementShape.getShape());
    }

    private void generateSegmentShape(double padding) {
        elementShape = new Viewport(placeNode(origin), placeNode(end), (origin.phiRatio(-1) + padding));
    }

    protected Point placeNode(NodeImage node) {
        return new Point((int) node.getElementShape().getX() + (int) node.getElementShape().getWidth() / 2,
                (int) node.getElementShape().getY() + (int) node.getElementShape().getHeight() / 2);
    }

    public void setRenderProperties(NodeImage origin, NodeImage end) {
        this.origin = origin;
        this.origin.addLinkedSegment(this);
        this.end = end;
        this.end.addLinkedSegment(this);
        generateSegmentShape(origin.phiRatio(1));
    }

    @Override
    public Displayable findHighestClick(Viewport viewport) {
        Displayable displayable = this;
        if (origin.getElementShape().intersects(viewport.getBounds2D())) {
            displayable = origin;
        }
        if (end.getElementShape().intersects(viewport.getBounds2D())) {
            displayable = end;
        }
        return displayable;
    }

    @Override
    void displayName(Graphics2D graphics2D) {
        //Get the center of the segment
        Point pointOrigin = placeNode(origin);
        Point pointEnd = placeNode(end);
        double theta = elementShape.getAngle(pointOrigin, pointEnd) + PI / 2;
        Viewport straitRectangle = new Viewport(elementShape.getBounds2D());
        double middleX = straitRectangle.getX() + straitRectangle.getWidth() / 2;
        double middleY = straitRectangle.getY() + straitRectangle.getHeight() / 2;
        //Place the segment name
        Viewport labelPosition = new Viewport(middleX, middleY);
        nameLabel.setRenderProperties(labelPosition);
        nameLabel.updateTextBounds(graphics2D);
        nameLabel.centerHorizontally();

        // Move it so it does not collide with the segment
        // Keep in mind that the bigger the name is, the farer it'll be from the segment
        double labelParting = (nameLabel.getElementShape().getDiagonalLength());
        middleX = nameLabel.relativeX + (Math.cos(theta) * labelParting);
        middleY = nameLabel.relativeY + (Math.sin(theta) * labelParting);

        //Paint it
        nameLabel.setRenderProperties(new Viewport(middleX, middleY));
        nameLabel.paint(graphics2D);
    }

    public NodeImage getOrigin() {
        return origin;
    }

    public NodeImage getEnd() {
        return end;
    }

    public void setOrigin(NodeImage origin) {
        this.origin = origin;
    }

    public void setEnd(NodeImage end) {
        this.end = end;
    }

    public int getTrackWidth() {
        PathIterator path = elementShape.getPathIterator(new AffineTransform());
        double[] pt1 = new double[6];
        double[] pt2 = new double[6];
        path.currentSegment(pt1);
        path.next();
        path.currentSegment(pt2);
        int trackWidth = (int) (Point.distance(pt1[0], pt1[1], pt2[0], pt2[1]) / 2.0);
        return trackWidth;
    }

    public Point getPositionFromProgression(double progression) {
        Point ptOrigin = origin.getCenterXY();
        Point ptEnd = end.getCenterXY();
        return new Point((int) (ptOrigin.getX() + (ptEnd.getX() - ptOrigin.getX()) * progression), (int) (ptOrigin.getY() + (ptEnd.getY() - ptOrigin.getY()) * progression));
    }

    @Override
    protected AffineTransform createTransform() {
        return new AffineTransform();
    }

    @Override
    public void setRenderProperties(Viewport viewport) {
    }

    public TriangularDistribution getTriangularDistribution(int identifer) {
        if (identifer == secondWayId) {
            return secondWayDistribution;
        } else {
            return triangularDistribution;
        }
    }

    public void setTriangularDistribution(TriangularDistribution triangularDistribution) {
        this.triangularDistribution = triangularDistribution;
    }

    @Override
    protected void renderBackground(Graphics2D graphics2D) {
    }

    public void removeLinkToNodes() {
        origin.removeLinkedSegment(this);
        end.removeLinkedSegment(this);
    }

    public void setTransitTime(int transitTime, int identifier) {
        if (identifier == secondWayId) {
            secondWaytransitTime = transitTime;
        } else {
            this.transitTime = transitTime;
        }
    }

    public int getTransitTime(int identifier) {
        if (identifier == secondWayId) {
            return secondWaytransitTime;
        } else {
            return transitTime;
        }
    }

    public void setTwoWay(int secondWayId, TriangularDistribution secondWayDistribution) {
        this.secondWayId = secondWayId;
        this.secondWayDistribution = secondWayDistribution;
        isTwoWay = true;
    }

    public boolean isTwoWay() {
        return isTwoWay;
    }

    public int getSecondWayId() {
        return secondWayId;
    }

    public void addTransit(int circuitID) {
        transitList.add(circuitID);
    }

    public void addReverseTransit(int circuitID) {
        reverseTransitList.add(circuitID);
    }

    public ArrayList<Integer> getTransitList() {
        return transitList;
    }

    public ArrayList<Integer> getReverseTransitList() {
        return reverseTransitList;
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
        if (origin != null) {
            origin.lockForCreation();
        }
        if (end != null) {
            end.lockForCreation();
        }
    }

    public void unlockAndUnselect() {
        lockForCreation = false;
        selectionColor = INVISIBLE;
        if (origin != null) {
            origin.unlockAndUnselect();
        }
        if (end != null) {
            end.unlockAndUnselect();
        }

    }

}
