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
import java.awt.geom.Ellipse2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import planeswalkers.simulatheure.ui.AssetManager;
import planeswalkers.simulatheure.ui.components.Text;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 *
 * @author Jean-Benoît
 */
public class CircularButton extends Button {

    protected boolean isNameVisible;
    protected double scaleRatio;
    protected double padding;
    protected double nameDeltaX;
    protected double nameDeltaY;

    /**
     *
     * @param name
     */
    public CircularButton(String name) {
        super(name);
        initButtonProperties();
    }

    public CircularButton(String name, String imageName) {
        super(name, imageName);
        initButtonProperties();
    }

    private void initButtonProperties() {
        displayColour = new Color(117, 163, 248);
        activeColour = displayColour;
        initNameLabel();
    }

    private void initNameLabel() {
        nameLabel = new Text("");
        nameLabel.setContent(imageName);
        nameLabel.setContentColor(Color.BLACK);
        nameLabel.setEditable(false);
        nameLabel.setSeparator("");
    }

    /**
     *
     * @param graphics2D
     */
    @Override
    public void paint(Graphics2D graphics2D) {
        try {

            Color backgroundColour = graphics2D.getColor();
//            paintButtonFrame(graphics2D);

            displayName(graphics2D);
            graphics2D.setColor(backgroundColour);
            graphics2D.fill(elementShape.getShape());

            saveGraphicsConfigurations(graphics2D);
            graphics2D.transform(createTransformation());
            AssetManager.getInstance().getUIToolComponent(imageName).render(graphics2D);
            restoreGraphicsConfigurations(graphics2D);
            paintVeil(graphics2D);

        } catch (SVGException ex) {
            Logger.getLogger(CircularButton.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void paintButtonFrame(Graphics2D graphics2D) {
        //**    Exclude this code and make a more intelligent border display in the appropriate upper class for all the needed elements **//
        graphics2D.setColor(new Color(70, 95, 180));
        Ellipse2D border = new Ellipse2D.Double(elementShape.getX() - 2, elementShape.getY() - 2, elementShape.getWidth() + 4, elementShape.getHeight() + 4);
        graphics2D.fill(border);
        //**   Stop excluding here   **//
    }

    private AffineTransform createTransformation() {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(relativeX + padding, relativeY + padding);
        affineTransform.scale(scaleRatio, scaleRatio);
        return affineTransform;
    }

    void displayName(Graphics2D graphics2D) {
//        nameLabel.setRenderProperties(new Viewport(relativeX + padding + nameDeltaX, relativeY + padding + nameDeltaY));
//        nameLabel.updateTextBounds(graphics2D);
//        nameLabel.resize(new Viewport(-1 / 2, 2));
//        nameLabel.paint(graphics2D);
    }

    /**
     *
     * @param viewport
     */
    @Override
    public void placeName(Viewport viewport) {
        nameDeltaX = viewport.getX();
        nameDeltaY = viewport.getY();
    }

    /**
     *
     * @param viewport
     */
    @Override
    public void setRenderProperties(planeswalkers.simulatheure.ui.components.Viewport viewport) {
        padding = (viewport.getBounds().width / Math.pow(PHI, 2)) / 2;
        scaleRatio = (viewport.getBounds().height - (2 * padding)) / BUTTON_SIZE;
        relativeX = viewport.getBounds().x;
        relativeY = viewport.getBounds().y;
        elementShape = new Viewport(new Ellipse2D.Double(relativeX, relativeY, viewport.getBounds().width, viewport.getBounds().height));
    }

}
