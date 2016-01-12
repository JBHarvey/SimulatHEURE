/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import planeswalkers.simulatheure.ui.components.SegmentImage;
import planeswalkers.simulatheure.ui.components.Text;
import planeswalkers.simulatheure.ui.components.TextBox;

/**
 *
 * @author Jean-Benoît
 */
public class PathBasics extends TextBox {

    private final Text pathName;
    private final Text origin;
    private final Text end;

    public PathBasics(String segmentName) {
        super(segmentName + PathBasics.class.getSimpleName(), 0, 0, defaultTextOffsets);
        pathName = new Text("Nom");
        origin = new Text("Origine");
        end = new Text("Fin");
        addChild(pathName);
        addChild(origin);
        addChild(end);
        horizontalOffset = 0;
    }

    public void open(SegmentImage segment) {
        pathName.setContent(segment.getDisplayName());
        origin.setContent(segment.getOrigin().getDisplayName());
        end.setContent(segment.getEnd().getDisplayName());
        origin.notEditable();
        end.notEditable();
    }

    public void openSecondWay(SegmentImage segment) {
        removeChild(pathName);
        origin.setContent(segment.getEnd().getDisplayName());
        end.setContent(segment.getOrigin().getDisplayName());
        origin.notEditable();
        end.notEditable();
    }

    public String getPathName() {
        return pathName.getContent();
    }
}
