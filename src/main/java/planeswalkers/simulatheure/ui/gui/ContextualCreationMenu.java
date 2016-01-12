/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.AddIntersectionButton;
import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.AddSegmentButton;
import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.AddStationButton;
import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.CircuitButton;
import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.CursorButton;
import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.TransportNeedButton;
import planeswalkers.simulatheure.ui.components.CircularBox;

/**
 *
 * @author Jean-Benoît
 */
public class ContextualCreationMenu extends CircularBox {

    /**
     *
     */
    public ContextualCreationMenu() {
        super(ContextualCreationMenu.class.getSimpleName());

        addButton(new AddSegmentButton());
        addButton(new TransportNeedButton());
        addButton(new CircuitButton());
        addButton(new CursorButton());
        addButton(new AddStationButton());
        addButton(new AddIntersectionButton());
        phaseChange = PI / 2;
    }

}
