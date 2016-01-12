/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import planeswalkers.simulatheure.ui.components.Buttons.PartialDelete;
import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.EditTransit;

/**
 *
 * @author Jean-Benoît
 */
public class TransitSummaryBox extends PopUpBox {

    Text transitName;
    EditTransit edit;
    PartialDelete delete;
    private Integer elementID;

    public TransitSummaryBox(TransitImage image, double x, double y) {
        super(image.getDisplayName() + "SummaryBox", x, y);
        elementID = image.getIdentifier();
        transitName = new Text("");
        transitName.setSeparator("");
        transitName.setContent(image.getDisplayName());
        transitName.notEditable();
        addText(transitName);
        setColorBarColor(image.getColor());
        addButtons();
    }

    protected void addButtons() {
        edit = new EditTransit();
        delete = new PartialDelete();
        setButtonSize(75);
        addButton(edit);
        addButton(delete);
    }

    public Integer getElementID() {
        return elementID;
    }
}
