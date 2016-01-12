/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import planeswalkers.simulatheure.ui.components.Text;
import planeswalkers.simulatheure.ui.components.TextBox;

/**
 *
 * @author Jean-Benoît
 */
public class SimulationSettings extends TextBox {

    private Text startTime;
    private Text endTime;
    private Text replication;

    public SimulationSettings(String name) {
        super(name, 0, 0, defaultTextOffsets);
        startTime = new Text("Temps de départ");
        endTime = new Text("Heure de fin");
        replication = new Text("Nombre de réplication");
        addChild(startTime);
        addChild(endTime);
        addChild(replication);
        horizontalOffset = 0;
    }

    public void open() {
        /**
         * Si vous vous rendez jusque là, vous pouvez vous baser sur ce modèle :
         *
         * public void open(SegmentImage segment) { elementID =
         * segment.getIdentifier();
         * pathName.setContent(segment.getDisplayName());
         * origin.setContent(segment.getOrigin().getDisplayName());
         * end.setContent(segment.getEnd().getDisplayName());
         * origin.notEditable(); end.notEditable(); }
         *
         *
         * L'idée étant aussi que y'a présentement aucun objet
         * "AnimationSettings" ou dequoi du genre pour voyager les infos...
         */
    }

    public double getStartTime() {
        return Double.parseDouble(startTime.getContent());
    }

    public double getEndTime() {
        return Double.parseDouble(endTime.getContent());
    }

    public double getReplication() {
        return Double.parseDouble(replication.getContent());
    }
}
