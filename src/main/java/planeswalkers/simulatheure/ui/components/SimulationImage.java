/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marc-Antoine Fortier
 */
public abstract class SimulationImage extends Displayable {

    protected SVGDiagram diagram;

    protected String displayName;
    private final int uniqueIdentifier;
    protected Color selectionColor;

    /**
     *
     * @param name
     * @param uniqueIdentifier
     */
    public SimulationImage(String name, int uniqueIdentifier) {
        super(String.valueOf(uniqueIdentifier));
        this.selectionColor = INVISIBLE;
        this.uniqueIdentifier = uniqueIdentifier;
        this.displayName = name;
        initNameLabel();
    }

    protected void initNameLabel() {
        nameLabel = new Text("");
        nameLabel.setContent(displayName);
        nameLabel.setContentColor(Color.BLACK);
        nameLabel.editable = false;
        nameLabel.setSeparator("");
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        try {
            AffineTransform savedTransform = graphics2D.getTransform();
            renderBackground(graphics2D);
            graphics2D.transform(createTransform());
            diagram.render(graphics2D);
            graphics2D.setTransform(savedTransform);
            displayName(graphics2D);

        } catch (SVGException ex) {
            Logger.getLogger(NodeImage.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected abstract AffineTransform createTransform();

    @Override
    public Displayable findHighestClick(Viewport viewport) {
        return this;
    }

    protected abstract void renderBackground(Graphics2D graphics2D);

    public Viewport getElementShape() {
        return elementShape;
    }

    /**
     *
     * @param viewport
     */
    @Override
    public abstract void setRenderProperties(planeswalkers.simulatheure.ui.components.Viewport viewport);

    public int getIdentifier() {
        return uniqueIdentifier;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void focus() {

    }

    @Override
    public void unfocus() {

    }

    @Override
    public void press() {
    }

    @Override
    public void unpress() {
    }

    public void select() {
        selectionColor = new Color(0, 124, 216, 126);
    }

    public void selectForCreation() {
        selectionColor = Color.YELLOW;
    }

    public void unselect() {
        selectionColor = INVISIBLE;
    }

    public void setModified() {
        System.out.println("You will need to save your simulation in order to keep " + getDisplayName() + " changes.");
    }

    public void setUnmodified() {
        System.out.println("Simulation saved, " + getDisplayName() + " changes too.");

    }

}
