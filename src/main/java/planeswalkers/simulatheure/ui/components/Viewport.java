/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A Viewport has the sames capabilities as a java.awt.Shape, except it is set
 * has a Rectangle.Double unless constructed otherwise.
 *
 * @author Jean-Benoît
 */
public class Viewport {

    Shape shape;

    /**
     * Builds a Rectangle.Double like Viewport
     *
     * @param x the x position
     * @param y the y position
     * @param width the rectangle's width
     * @param height the rectangle's height
     */
    public Viewport(double x, double y, double width, double height) {
        shape = new Rectangle.Double(x, y, width, height);
    }

    /**
     * Builds a Viewport as a copy of the shaped passed in parameters.
     *
     * @param shape the shape to be taken by the Viewport
     */
    public Viewport(Shape shape) {
        this.shape = shape;
    }

    /**
     * Builds a 1 X 1 Rectangle.Double at the specified x and y positions.
     *
     * @param x the x position
     * @param y the y position
     */
    public Viewport(double x, double y) {
        shape = new Rectangle.Double(x, y, 1, 1);
    }

    public Viewport(Point pt1, Point pt2, double scaleFactor) {
        Point ptA = new Point();
        Point ptB = new Point();
        Point ptC = new Point();
        Point ptD = new Point();
        ptA.setLocation(pt1.getX() + scaleFactor / 5.0 * Math.cos((Math.PI / 2.0) - (getAngle(pt1, pt2))),
                pt1.getY() - scaleFactor / 5.0 * Math.sin((Math.PI / 2.0) - (getAngle(pt1, pt2))));
        ptB.setLocation(pt2.getX() - scaleFactor / 5.0 * Math.cos((Math.PI / 2.0) - (getAngle(pt1, pt2))),
                pt2.getY() + scaleFactor / 5.0 * Math.sin((Math.PI / 2.0) - (getAngle(pt1, pt2))));
        ptC.setLocation(pt1.getX() - scaleFactor / 5.0 * Math.cos((Math.PI / 2.0) - (getAngle(pt1, pt2))),
                pt1.getY() + scaleFactor / 5.0 * Math.sin((Math.PI / 2.0) - (getAngle(pt1, pt2))));
        ptD.setLocation(pt2.getX() + scaleFactor / 5.0 * Math.cos((Math.PI / 2.0) - (getAngle(pt1, pt2))),
                pt2.getY() - scaleFactor / 5.0 * Math.sin((Math.PI / 2.0) - (getAngle(pt1, pt2))));
        int[] xs = {(int) ptA.getX(), (int) ptC.getX(), (int) ptB.getX(), (int) ptD.getX()};
        int[] ys = {(int) ptA.getY(), (int) ptC.getY(), (int) ptB.getY(), (int) ptD.getY()};
        shape = new Polygon(xs, ys, 4);

    }

    public static Line2D getLineWithOffset(Line2D line, double scaleFactor) {
        Point pt1 = new Point((int) line.getP1().getX(), (int) line.getP1().getY());
        Point pt2 = new Point((int) line.getP2().getX(), (int) line.getP2().getY());
        pt1.setLocation(pt1.getX() + scaleFactor * Math.cos((Math.PI / 2.0) - (getAngle(pt1, pt2))),
                pt1.getY() - scaleFactor * Math.sin((Math.PI / 2.0) - (getAngle(pt1, pt2))));
        pt2.setLocation(pt2.getX() + scaleFactor * Math.cos((Math.PI / 2.0) - (getAngle(pt1, pt2))),
                pt2.getY() - scaleFactor * Math.sin((Math.PI / 2.0) - (getAngle(pt1, pt2))));
        return new Line2D.Double(pt1, pt2);
    }

    /**
     * Allows to get the Viewport's current shape
     *
     * @return the shape of the viewport
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Creates a Random 1 X 1 rectangle somewhere in the Frame's dimensions
     */
    public Viewport() {
        shape = new Rectangle.Double(Math.random() * 1440, Math.random() * 900, 1, 1);
    }

    /**
     * Return the X position of the Viewport
     *
     * @return the x position
     */
    public double getX() {
        return shape.getBounds2D().getX();
    }

    /**
     * Return the y position of the Viewport
     *
     * @return the y position
     */
    public double getY() {
        return shape.getBounds2D().getY();
    }

    /**
     * Allow to get the width of the shape
     *
     * @return the width of the rectangle squaring the shape of the viewport
     */
    public double getWidth() {
        return shape.getBounds2D().getWidth();
    }

    /**
     * Allow to get the width of the shape
     *
     * @return the height of the rectangle squaring the shape of the viewport
     */
    public double getHeight() {
        return shape.getBounds2D().getHeight();
    }

    /**
     * Returns an integer Rectangle that completely encloses the Shape. Note
     * that there is no guarantee that the returned Rectangle is the smallest
     * bounding box that encloses the Shape, only that the Shape lies entirely
     * within the indicated Rectangle. The returned Rectangle might also fail to
     * completely enclose the Shape if the Shape overflows the limited range of
     * the integer data type. The getBounds2D method generally returns a tighter
     * bounding box due to its greater flexibility in representation. Note that
     * the definition of insideness can lead to situations where points on the
     * defining outline of the shape may not be considered contained in the
     * returned bounds object, but only in cases where those points are also not
     * considered contained in the original shape. If a point is inside the
     * shape according to the contains(point) method, then it must be inside the
     * returned Rectangle bounds object according to the contains(point) method
     * of the bounds. Specifically: shape.contains(x,y) requires
     * bounds.contains(x,y) If a point is not inside the shape, then it might
     * still be contained in the bounds object: bounds.contains(x,y) does not
     * imply shape.contains(x,y) Returns: an integer Rectangle that completely
     * encloses the Shape
     *
     * @return
     * @returnan integer Rectangle that completely encloses the Shape
     */
    public Rectangle getBounds() {
        return shape.getBounds();
    }

    /**
     * Returns a high precision and more accurate bounding box of the Shape than
     * the getBounds method. Note that there is no guarantee that the returned
     * Rectangle2D is the smallest bounding box that encloses the Shape, only
     * that the Shape lies entirely within the indicated Rectangle2D. The
     * bounding box returned by this method is usually tighter than that
     * returned by the getBounds method and never fails due to overflow problems
     * since the return value can be an instance of the Rectangle2D that uses
     * double precision values to store the dimensions. Note that the definition
     * of insideness can lead to situations where points on the defining outline
     * of the shape may not be considered contained in the returned bounds
     * object, but only in cases where those points are also not considered
     * contained in the original shape. If a point is inside the shape according
     * to the contains(point) method, then it must be inside the returned
     * Rectangle2D bounds object according to the contains(point) method of the
     * bounds. Specifically: shape.contains(p) requires bounds.contains(p) If a
     * point is not inside the shape, then it might still be contained in the
     * bounds object: bounds.contains(p) does not imply shape.contains(p)
     *
     * @return an instance of Rectangle2D that is a high-precision bounding box
     * of the Shape.
     *
     */
    public Rectangle2D getBounds2D() {
        return shape.getBounds2D();
    }

    /**
     * Tests if the specified coordinates are inside the boundary of the Shape,
     * as described by the definition of insideness.
     *
     * @param x - the specified X coordinate to be tested @param y - the
     * specified Y coordinate to be tested
     * @param y
     * @return true if the specified coordinates are inside the Shape boundary;
     * false otherwise.
     */
    public boolean contains(double x, double y) {
        return shape.contains(x, y);
    }

    /**
     * Tests if a specified Point2D is inside the boundary of the Shape, as
     * described by the definition of insideness.
     *
     * @param p p - the specified Point2D to be tested
     * @return true if the specified Point2D is inside the boundary of the
     * Shape; false otherwise.
     *
     *
     */
    public boolean contains(Point2D p) {
        return shape.contains(p);
    }

    /**
     *
     * Tests if the interior of the Shape intersects the interior of a specified
     * rectangular area. The rectangular area is considered to intersect the
     * Shape if any point is contained in both the interior of the Shape and the
     * specified rectangular area. The Shape.intersects() method allows a Shape
     * implementation to conservatively return true when: there is a high
     * probability that the rectangular area and the Shape intersect, but the
     * calculations to accurately determine this intersection are prohibitively
     * expensive. This means that for some Shapes this method might return true
     * even though the rectangular area does not intersect the Shape. The Area
     * class performs more accurate computations of geometric intersection than
     * most Shape objects and therefore can be used if a more precise answer is
     * required.
     *
     * @param x - the X coordinate of the upper-left corner of the specified
     * rectangular area
     * @param y - the Y coordinate of the upper-left corner of the specified
     * rectangular area
     * @param w - the width of the specified rectangular area
     * @param h - the height of the specified rectangular area
     *
     * @return true if the interior of the Shape and the interior of the
     * rectangular area intersect, or are both highly likely to intersect and
     * intersection calculations would be too expensive to perform; false
     * otherwise.
     *
     *
     */
    public boolean intersects(double x, double y, double w, double h) {
        return shape.intersects(x, y, w, h);
    }

    /**
     * Tests if the interior of the Shape intersects the interior of a specified
     * Rectangle2D. The Shape.intersects() method allows a Shape implementation
     * to conservatively return true when: there is a high probability that the
     * Rectangle2D and the Shape intersect, but the calculations to accurately
     * determine this intersection are prohibitively expensive. This means that
     * for some Shapes this method might return true even though the Rectangle2D
     * does not intersect the Shape. The Area class performs more accurate
     * computations of geometric intersection than most Shape objects and
     * therefore can be used if a more precise answer is required.
     *
     * @param r - the specified Rectangle2D
     *
     * @return true if the interior of the Shape and the interior of the
     * specified Rectangle2D intersect, or are both highly likely to intersect
     * and intersection calculations would be too expensive to perform; false
     * otherwise.
     *
     */
    public boolean intersects(Rectangle2D r) {
        return shape.intersects(r);
    }

    /**
     * Tests if the interior of the Shape entirely contains the specified
     * rectangular area. All coordinates that lie inside the rectangular area
     * must lie within the Shape for the entire rectangular area to be
     * considered contained within the Shape. The Shape.contains() method allows
     * a Shape implementation to conservatively return false when: the intersect
     * method returns true and the calculations to determine whether or not the
     * Shape entirely contains the rectangular area are prohibitively expensive.
     * This means that for some Shapes this method might return false even
     * though the Shape contains the rectangular area. The Area class performs
     * more accurate geometric computations than most Shape objects and
     * therefore can be used if a more precise answer is required.
     *
     * @param x - the X coordinate of the upper-left corner of the specified
     * rectangular area
     * @param y - the Y coordinate of the upper-left corner of the specified
     * rectangular area
     * @param w - the width of the specified rectangular area
     * @param h - the height of the specified rectangular area
     *
     * @return true if the interior of the Shape entirely contains the specified
     * rectangular area; false otherwise or, if the Shape contains the
     * rectangular area and the intersects method returns true and the
     * containment calculations would be too expensive to perform.
     */
    public boolean contains(double x, double y, double w, double h) {
        return shape.contains(x, y, w, h);
    }

    /**
     * Tests if the interior of the Shape entirely contains the specified
     * Rectangle2D. The Shape.contains() method allows a Shape implementation to
     * conservatively return false when: the intersect method returns true and
     * the calculations to determine whether or not the Shape entirely contains
     * the Rectangle2D are prohibitively expensive. This means that for some
     * Shapes this method might return false even though the Shape contains the
     * Rectangle2D. The Area class performs more accurate geometric computations
     * than most Shape objects and therefore can be used if a more precise
     * answer is required.
     *
     * @param r - The specified Rectangle2D
     *
     * @return true if the interior of the Shape entirely contains the
     * Rectangle2D; false otherwise or, if the Shape contains the Rectangle2D
     * and the intersects method returns true and the containment calculations
     * would be too expensive to perform.
     *
     */
    public boolean contains(Rectangle2D r) {
        return shape.contains(r);
    }

    /**
     * Returns an iterator object that iterates along the Shape boundary and
     * provides access to the geometry of the Shape outline. If an optional
     * AffineTransform is specified, the coordinates returned in the iteration
     * are transformed accordingly. Each call to this method returns a fresh
     * PathIterator object that traverses the geometry of the Shape object
     * independently from any other PathIterator objects in use at the same
     * time. It is recommended, but not guaranteed, that objects implementing
     * the Shape interface isolate iterations that are in process from any
     * changes that might occur to the original object's geometry during such
     * iterations.
     *
     * @param at - an optional AffineTransform to be applied to the coordinates
     * as they are returned in the iteration, or null if untransformed
     * coordinates are desired
     *
     * @return a new PathIterator object, which independently traverses the
     * geometry of the Shape.
     *
     */
    public PathIterator getPathIterator(AffineTransform at) {
        return shape.getPathIterator(at);
    }

    /**
     * Returns an iterator object that iterates along the Shape boundary and
     * provides access to a flattened view of the Shape outline geometry. Only
     * SEG_MOVETO, SEG_LINETO, and SEG_CLOSE point types are returned by the
     * iterator. If an optional AffineTransform is specified, the coordinates
     * returned in the iteration are transformed accordingly. The amount of
     * subdivision of the curved segments is controlled by the flatness
     * parameter, which specifies the maximum distance that any point on the
     * unflattened transformed curve can deviate from the returned flattened
     * path segments. Note that a limit on the accuracy of the flattened path
     * might be silently imposed, causing very small flattening parameters to be
     * treated as larger values. This limit, if there is one, is defined by the
     * particular implementation that is used. Each call to this method returns
     * a fresh PathIterator object that traverses the Shape object geometry
     * independently from any other PathIterator objects in use at the same
     * time. It is recommended, but not guaranteed, that objects implementing
     * the Shape interface isolate iterations that are in process from any
     * changes that might occur to the original object's geometry during such
     * iterations.
     *
     * @param at - an optional AffineTransform to be applied to the coordinates
     * as they are returned in the iteration, or null if untransformed
     * coordinates are desired
     * @param flatness - the maximum distance that the line segments used to
     * approximate the curved segments are allowed to deviate from any point on
     * the original curve
     *
     * @return a new PathIterator that independently traverses a flattened view
     * of the geometry of the Shape.
     *
     *
     *
     *
     */
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return shape.getPathIterator(at, flatness);
    }

    public Point.Double toPoint() {
        return new Point.Double(getX(), getY());
    }

    public double getAngle() {
        double x1 = getX();
        double y1 = getY();
        double x2 = getX() + getWidth();
        double y2 = getY() + getHeight();

        return Math.atan2(x2 - x1, y1 - y2) + Math.PI;
    }

    public static double getAngle(Point pt1, Point pt2) {
        double x1 = pt1.getX();
        double y1 = pt1.getY();
        double x2 = pt2.getX();
        double y2 = pt2.getY();

        return Math.atan2(x2 - x1, y1 - y2) + Math.PI / 2;
    }

    /**
     * Calculates the diagonal of the rectangle2D binding the Viewport
     *
     * @return The length of the diagonal.
     */
    public double getDiagonalLength() {
        double x1 = getX();
        double y1 = getY();
        double x2 = getX() + getWidth();
        double y2 = getY() + getHeight();

        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
    }

    public Point getCenterXY() {
        return new Point((int) (getX() + getWidth() / 2.0), (int) (getY() + getHeight() / 2.0));
    }
}
