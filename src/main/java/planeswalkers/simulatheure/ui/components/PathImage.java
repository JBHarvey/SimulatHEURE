/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

/**
 *
 * @author Marc-Antoine Fortier
 */
public class PathImage {

    private int segmentID;
    private SegmentImage segmentImage;

    public PathImage(int segmentID, SegmentImage segmentImage) {
        this.segmentID = segmentID;
        this.segmentImage = segmentImage;
    }

    public int getSegmentID() {
        return segmentID;
    }

    public SegmentImage getSegmentImage() {
        return segmentImage;
    }
}
