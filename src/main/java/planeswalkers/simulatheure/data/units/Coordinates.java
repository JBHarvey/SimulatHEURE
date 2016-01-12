/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.units;

import com.jhlabs.map.proj.MercatorProjection;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 *
 * @author Jean-Benoît, Marc-Antoine Fortier
 */
public class Coordinates implements Serializable{

    private final double latitude;
    private final double longitude;
    private final double elevation;

    /**
     * The location of a real object in the form of GPS coordinates.
     *
     * @param latitude the latitude in decimal degrees.
     * @param longitude the longitude in decimal degrees.
     * @param elevation the elevation in meters above sea level.
     */
    public Coordinates(double latitude, double longitude, double elevation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }

    /**
     * Return the latitude of the coordinates.
     *
     * @return The latitude of the coordinates.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Return the longitude of the coordinates.
     *
     * @return The longitude of the coordinates.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param coordinates
     * @return
     */
    public boolean isEqual(Coordinates coordinates) {
        return (this.latitude == coordinates.getLatitude()
                && this.longitude == coordinates.getLongitude());
    }

    /**
     *
     * @return
     */
    public Point2D.Double getXY() {
        MercatorProjection projection = new MercatorProjection();

        // Convert to radian.
        double latitudeRad = latitude * Math.PI / 180;
        double longitudeRad = longitude * Math.PI / 180;

        Point2D.Double d = projection.project(longitudeRad, latitudeRad, new Point2D.Double());
        d.y = -d.y;
        return d;
    }

    /**
     *
     * @param xy
     * @return
     */
    public static Coordinates getCoordinatesFromXY(Point2D.Double xy) {
        MercatorProjection projection = new MercatorProjection();

        Point2D.Double coordinatesRad = projection.projectInverse(xy.getX(), -xy.getY(), new Point2D.Double());

        return new Coordinates(coordinatesRad.getY() * 180 / Math.PI, coordinatesRad.getX() * 180 / Math.PI, 0);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Latitude: " + latitude + ", Longitude: " + longitude;
    }

    public Coordinates getDelta(Coordinates lastCoordinates) {
        return new Coordinates(latitude - lastCoordinates.latitude, longitude - lastCoordinates.longitude, 0);
    }
}
