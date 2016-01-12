/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components.Buttons;

import com.kitfox.svg.SVGException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.logging.Level;
import java.util.logging.Logger;
import planeswalkers.simulatheure.ui.AssetManager;

/**
 *
 * @author Marc-Antoine Fortier
 */
public class SquareButton extends Button {

    /**
     *
     */
    protected boolean isNameVisible;
    protected double scaleRatio, padding;

    /**
     *
     * @param name
     */
    public SquareButton(String name) {
        super(name);
        isNameVisible = false;

        displayColour = new Color(50, 50, 50);
        activeColour = displayColour;
        setOpaque(false);
    }

    /**
     *
     * @param graphics2D
     */
    @Override
    public void paint(Graphics2D graphics2D) {
        try {
            saveGraphicsConfigurations(graphics2D);

            graphics2D.transform(createTransformation());
            AssetManager.getInstance().getUIToolComponent(name).render(graphics2D);
            restoreGraphicsConfigurations(graphics2D);
            paintVeil(graphics2D);

        } catch (SVGException ex) {
            Logger.getLogger(SquareButton.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private AffineTransform createTransformation() {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(elementShape.getBounds().x + padding, elementShape.getBounds().y + padding);
        affineTransform.scale(scaleRatio, scaleRatio);
        return affineTransform;
    }

    /**
     *
     * @param viewport
     */
    @Override
    public void setRenderProperties(planeswalkers.simulatheure.ui.components.Viewport viewport) {
        padding = (viewport.getWidth() / Math.pow(PHI, 2)) / 2;
        scaleRatio = (viewport.getHeight() - (2 * padding)) / BUTTON_SIZE;
        elementShape = viewport;
    }

    void displayName() {
    }

}
