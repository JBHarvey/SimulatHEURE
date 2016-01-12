/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import planeswalkers.simulatheure.ui.components.Box;
import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.Add;
import planeswalkers.simulatheure.ui.components.NetworkEditionBox;
import planeswalkers.simulatheure.ui.components.TransitImage;
import planeswalkers.simulatheure.ui.components.TransitSummaryBox;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 *
 * @author Jean-Benoît
 */
public class TransitGatheringBox extends NetworkEditionBox {

    boolean appendingCircuit = false;

    public TransitGatheringBox(double x, double y) {
        super(TransitGatheringBox.class.getSimpleName(), x, y);
        boxWidth = 350;
    }

    public void startAppendCircuit() {
        removeAllChildren();
        appendingCircuit = true;
    }

    public void startAppendTransportNeed() {
        removeAllChildren();
        appendingCircuit = false;
    }

    public boolean isAppendingCircuit() {
        return appendingCircuit;
    }

    public void append(TransitImage image) {
        addChild(new TransitSummaryBox(image, relativeX, relativeY + getBoxHeight() - verticalPadding));
    }

    @Override
    protected void addNeededButtons() {
        editionButtons.addButton(new Add());
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
        addPlusButton();
    }

    @Override
    public void close() {
        removeAllChildren();
    }

}
