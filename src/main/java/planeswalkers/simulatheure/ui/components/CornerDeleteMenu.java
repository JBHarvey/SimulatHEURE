/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import planeswalkers.simulatheure.ui.components.Buttons.DeleteButton;

/**
 *
 * @author Jean-Benoît
 */
public class CornerDeleteMenu extends CircularBox {

    public CornerDeleteMenu() {
        super(CornerDeleteMenu.class.getSimpleName());
        addButton(new DeleteButton());
        phaseChange = (3 * PI / 4);
        frameColor = INVISIBLE;
        backgroundColour = INVISIBLE;
    }

}
