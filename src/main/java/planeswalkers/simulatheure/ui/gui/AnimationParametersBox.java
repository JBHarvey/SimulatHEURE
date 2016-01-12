/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import planeswalkers.simulatheure.data.SimulationElement;
import planeswalkers.simulatheure.ui.components.AnimatorImage;
import planeswalkers.simulatheure.ui.components.NetworkInformationBox;
import planeswalkers.simulatheure.ui.components.NodeImage;
import planeswalkers.simulatheure.ui.components.Text;
import planeswalkers.simulatheure.ui.components.TextBox;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 *
 * @author Jean-Benoît
 */
public class AnimationParametersBox extends NetworkInformationBox {

    private final Parameters parameters;

    public AnimationParametersBox(AnimatorImage animation) {
        super(AnimationParametersBox.class.getSimpleName(), new NodeImage("Decoy", -1, null) {
        });
        parameters = new Parameters(animation);
        addChild(parameters);
        elementShape = new Viewport(0, 0, boxWidth, 0);
        relativeX = elementShape.getX();
        relativeY = elementShape.getY();
    }

    public AnimatorImage getParametrisedAnimation() {
        return parameters.getParametrisedAnimation();
    }

    public double getStartingTime() {
        return parameters.getStartingTime();
    }

    public double getEndTime() {
        return parameters.getEndTime();
    }

    public int getReplication() {
        return parameters.getReplication();
    }

    @Override
    public SimulationElement getModifiedElement() {
        return null;
    }

    private class Parameters extends TextBox {

        private final Text startingTime;
        private final Text endTime;
        private final Text replication;
        private AnimatorImage animation;

        public Parameters(AnimatorImage image) {
            super(Parameters.class.getSimpleName(), 0, 0, defaultTextOffsets);
            startingTime = new Text("Heure de début");
            endTime = new Text("Heure de fin");
            replication = new Text("Nombre de réplication");

            animation = image;

            startingTime.setContent(secondsToText(animation.getStartingTime()));
            endTime.setContent(secondsToText(animation.getEndTime()));
            replication.setContent(String.valueOf(animation.getNumberOfReplication()));

            addChild(startingTime);
            addChild(endTime);
            addChild(replication);
            horizontalOffset = 0;
        }

        public AnimatorImage getParametrisedAnimation() {
            animation.setStartingTime(getStartingTime());
            animation.setEndTime(getEndTime());
            animation.setNumberOfReplication(getReplication());
            return animation;
        }

        public int getStartingTime() {
            return textToSeconds(startingTime.getContent());
        }

        public int getEndTime() {
            return textToSeconds(endTime.getContent());
        }

        public int getReplication() {
            return Integer.parseInt(replication.getContent());
        }
    }
}
