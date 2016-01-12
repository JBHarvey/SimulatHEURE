/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import planeswalkers.simulatheure.data.networkelement.Intersection;
import planeswalkers.simulatheure.data.networkelement.Station;
import planeswalkers.simulatheure.data.units.Coordinates;
import planeswalkers.simulatheure.ui.components.NodeImage;
import planeswalkers.simulatheure.ui.components.SimulationImage;
import planeswalkers.simulatheure.ui.components.Text;
import planeswalkers.simulatheure.ui.components.TextBox;

/**
 *
 * @author Jean-Benoît
 */
public class ElementPositionInfo extends TextBox {

    private final Text elementName;
    private final Text latitude;
    private final Text longitude;
    private Integer elementID;

    public ElementPositionInfo(String name) {
        super(name + "InformationBox", 0, 0, defaultTextOffsets);
        elementName = new Text("Name");
        latitude = new Text("Longitude");
        longitude = new Text("Latitude");
        addChild(elementName);
        addChild(latitude);
        addChild(longitude);
    }

    public <S extends SimulationImage> void open(S image) {
        NodeImage node = (NodeImage) image;
        elementID = node.getIdentifier();
        elementName.setContent(node.getDisplayName());
        latitude.setContent(String.valueOf(node.getCoordinates().getLongitude()));
        longitude.setContent(String.valueOf(node.getCoordinates().getLatitude()));
        latitude.notEditable();
        longitude.notEditable();
    }

    public Station getStation() {
        return new Station(elementName.getContent(), elementID, getCoordinates());
    }

    public Intersection getIntersection() {
        return new Intersection(elementName.getContent(), elementID, getCoordinates());
    }

    private Coordinates getCoordinates() {
        double latitudeValue = Double.parseDouble(latitude.getContent());
        double longitudeValue = Double.parseDouble(longitude.getContent());
        return new Coordinates(longitudeValue, latitudeValue, 0);
    }
}
