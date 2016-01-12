/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import planeswalkers.simulatheure.data.units.Coordinates;
import planeswalkers.simulatheure.ui.DataReceiver;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 * Bind to the work plane, it defined which and how the elements are going to
 * appear on it.
 *
 * @author Marc-Antoine Fortier, Antoine Gregoire-Slight
 */
public final class Camera {

    private static final double ZOOM_STEP = 0.50;
    private static final double MOVE_STEP = 0.00005;

    private Rectangle2D boundaries;
    private Coordinates coordinates;

    /**
     * The latitude of where the camera (his upper-left corner) is looking in
     * the real world.
     */
    protected double latitude;

    /**
     * The longitude of where the camera (his upper-left corner) is looking in
     * the real world.
     */
    protected double longitude;

    /**
     * The magnifying factor of the camera lense.
     */
    protected double zoomRatio = 15.0;

    private boolean stateChanged = true;
    private AffineTransform affineTransform;

    /**
     * Camera Constructor, creates a camera object.
     *
     * @param dataReceiver The data receiver to communicate with the model.
     */
    public Camera(DataReceiver dataReceiver) {
        this.coordinates = (new Coordinates(46.7830306, -71.2769762, 0));
        longitude = coordinates.getLongitude();
        latitude = coordinates.getLatitude();
        boundaries = new Rectangle2D.Double();
        fetchCameraBoundsXY();
    }

    /**
     * Scales up the camera of a single zoom step.
     */
    public void zoomIn() {
        if (zoomRatio < 70.0) {
            this.zoomRatio += ZOOM_STEP;
            fetchCameraBoundsXY();
            stateChanged = true;
        }
    }

    /**
     * Scales down the camera of a single zoom step.
     */
    public void zoomOut() {
        if (zoomRatio > 5.0) {
            this.zoomRatio -= ZOOM_STEP;
            fetchCameraBoundsXY();
            stateChanged = true;
        }
    }

    /**
     * Increments longitude of a move step.
     */
    public void moveEast() {
        this.longitude += MOVE_STEP;
        coordinates = new Coordinates(latitude, longitude, 0);
        fetchCameraBoundsXY();
        stateChanged = true;
    }

    /**
     * Decrements longitude of a move step.
     */
    public void moveWest() {
        this.longitude -= MOVE_STEP;
        coordinates = new Coordinates(latitude, longitude, 0);
        fetchCameraBoundsXY();
        stateChanged = true;
    }

    /**
     * Increments latitude of a move step.
     */
    public void moveNorth() {
        this.latitude += MOVE_STEP;
        coordinates = new Coordinates(latitude, longitude, 0);
        fetchCameraBoundsXY();
        stateChanged = true;
    }

    /**
     * Decrements latitude of a move step.
     */
    public void moveSouth() {
        this.latitude -= MOVE_STEP;
        coordinates = new Coordinates(latitude, longitude, 0);
        fetchCameraBoundsXY();
        stateChanged = true;
    }

//    /**
//     * Selects the network elements that intersects the camera projection.
//     *
//     * @param workPlaneFrame The bounds of the work plane in pixel.
//     * @return Only the simulation elements that are where the camera is
//     * looking.
//     */
//    public HashMap<String, HashMap<Integer, SimulationElement>> fetchSeenElements(Viewport workPlaneFrame) {
//
//        HashMap<String, HashMap<Integer, SimulationElement>> simulationElement = dataReceiver.getSimulationElements();
//
//        for (SimulationElement element : simulationElement.get(Station.class.getSimpleName()).values()) {
//            Node station = (Node) element;
//            if (!isInFrame(Station.class.getSimpleName(), workPlaneFrame, station)) {
//                simulationElement.get(Station.class.getSimpleName()).remove(element.getIdentifier());
//            }
//        }
//
//        for (SimulationElement element : simulationElement.get(Intersection.class.getSimpleName()).values()) {
//            Node intersection = (Node) element;
//            if (!isInFrame(Intersection.class.getSimpleName(), workPlaneFrame, intersection)) {
//                simulationElement.get(Intersection.class.getSimpleName()).remove(element.getIdentifier());
//            }
//        }
//
//        return simulationElement;
//    }
//
//    private boolean isInFrame(String type, Viewport workPlaneFrame, Node element) {
//
//        boolean isInFrame = false;
//        Rectangle2D elementShape = (Rectangle2D) AssetManager.getInstance().getShape(type).getBounds();
//        elementShape = new Rectangle2D.Double(element.getCoordinates().getXY().x, element.getCoordinates().getXY().y,
//                elementShape.getWidth() * WorkPlane.PREFERRED_SIMULATION_COMPONENT_SCALE * boundaries.getWidth() / workPlaneFrame.getWidth(),
//                elementShape.getHeight() * WorkPlane.PREFERRED_SIMULATION_COMPONENT_SCALE * boundaries.getHeight() / workPlaneFrame.getHeight());
//
//        if (boundaries.intersects(elementShape)) {
//            isInFrame = true;
//        }
//        return isInFrame;
//    }
    public void moveToCoordinates(Coordinates coordinates) {
        this.longitude = coordinates.getLongitude();
        this.latitude = coordinates.getLatitude();
        this.coordinates = coordinates;
        fetchCameraBoundsXY();
        stateChanged = true;
    }

    /**
     * Computes the bounds of the camera frame in XY world coordinates (Mercator
     * projection).
     */
    public void fetchCameraBoundsXY() {
        Point2D xy = coordinates.getXY();
        double height = (0.0011464845646099 * 15.0 / zoomRatio);
        double width = ((double) SimulatorScreen.WIDTH / (double) SimulatorScreen.HEIGHT) * height;
        boundaries = new Rectangle2D.Double(xy.getX(), xy.getY(), width, height);
    }

    /**
     *
     * @param workPlaneFrame
     * @return An affine transform to transform the camera bounds rectangle (XY
     * coordinates from Mercator projection) to the work plane bounds rectangle
     * (XY coordinates in pixel).
     */
    public AffineTransform transformCameraBoundariesToWorkPlane(Viewport workPlaneFrame) {
        if (stateChanged) {
            affineTransform = new AffineTransform();
            double widthRatio = (double) workPlaneFrame.getWidth() / boundaries.getWidth();
            double heightRatio = (double) workPlaneFrame.getHeight() / boundaries.getHeight();
            affineTransform.scale(widthRatio, heightRatio);
            affineTransform.translate(-boundaries.getMinX(), -boundaries.getMinY());
            stateChanged = false;
        }
        return affineTransform;
    }

    public Rectangle2D transformOffsetBoundariesToWorkPlane(Viewport workPlaneFrame, Rectangle2D offset) {
        double widthRatio = (double) workPlaneFrame.getWidth() / boundaries.getWidth();
        double heightRatio = (double) workPlaneFrame.getHeight() / boundaries.getHeight();
        Rectangle2D rect = new Rectangle2D.Double(0, 0, -offset.getWidth() * widthRatio, -offset.getHeight() * heightRatio);
        return rect;
    }

    /**
     *
     * @param workPlaneFrame
     * @return An affine transform to transform the work plane bounds rectangle
     * (XY coordinates in pixel) to the camera bounds rectangle (XY coordinates
     * from Mercator projection).
     */
    public AffineTransform transformWorkPlaneToCameraBoundaries(Viewport workPlaneFrame) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(boundaries.getMinX(), boundaries.getMinY());
        double widthRatio = boundaries.getWidth() / (double) workPlaneFrame.getWidth();
        double heightRatio = boundaries.getHeight() / (double) workPlaneFrame.getHeight();
        affineTransform.scale(widthRatio, heightRatio);
        return affineTransform;
    }

    public double getZoomRatio() {
        return zoomRatio;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

}
