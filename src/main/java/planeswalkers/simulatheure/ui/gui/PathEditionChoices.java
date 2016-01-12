/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import planeswalkers.simulatheure.ui.components.Buttons.CancelButton;
import planeswalkers.simulatheure.ui.components.Buttons.ConfirmButton;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.CircularUndo;
import planeswalkers.simulatheure.ui.components.CircularBox;

/**
 *
 * @author Jean-Benoit
 */
public class PathEditionChoices extends CircularBox {

    /**
     *
     */
    public PathEditionChoices() {
        super(ContextualCreationMenu.class.getSimpleName());

        addButton(new ConfirmButton());
        addButton(new CancelButton());
        addButton(new CircularUndo());

    }

}
