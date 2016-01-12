/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import planeswalkers.simulatheure.ui.components.Box;
import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.CircuitButton;
import planeswalkers.simulatheure.ui.components.CircuitImage;
import planeswalkers.simulatheure.ui.components.NetworkEditionBox;
import planeswalkers.simulatheure.ui.components.TransitSummaryBox;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 *
 * @author Jean-Benoît
 */
public class CircuitChooser extends NetworkEditionBox {

    public CircuitChooser(double x, double y) {
        super(CircuitChooser.class.getSimpleName(), x, y);
        boxWidth = 300;
    }

    public void startAppendCircuit() {
        removeAllChildren();
    }

    public void append(CircuitImage image) {
        addChild(new CircuitChoice(image, relativeX, relativeY + getBoxHeight() - verticalPadding));
        elementShape = new Viewport(relativeX, relativeY, boxWidth + (2 * horizontalPadding), (getBoxHeight() + verticalPadding));
    }

    @Override
    protected void addNeededButtons() {
    }

    @Override
    public void setRenderProperties(Viewport viewport) {

        double childX = relativeX + horizontalPadding;
        double childY = relativeY + verticalPadding;
        double childWidth = boxWidth + 2 * horizontalPadding;
        double childHeigth;
        for (Box box : getContainedElement().values()) {
            childHeigth = box.getBoxHeight();
            box.setRenderProperties(new Viewport(childX, childY, childWidth, childHeigth));
            childY += childHeigth;
        }
    }

    @Override
    public void close() {
        removeAllChildren();
    }

    protected class CircuitChoice extends TransitSummaryBox {

        CircuitButton circuit;
        private int circuitID;

        public CircuitChoice(CircuitImage image, double x, double y) {
            super(image, x, y);
        }

        @Override
        protected void addButtons() {
            circuit = new CircuitButton();
            setButtonSize(75);
            addButton(circuit);
        }

        public int getCircuitID() {
            return circuitID;
        }
    }
}
