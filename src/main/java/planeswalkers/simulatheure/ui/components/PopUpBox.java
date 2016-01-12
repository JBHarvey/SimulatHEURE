/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.Color;
import java.awt.Graphics2D;
import planeswalkers.simulatheure.ui.components.Buttons.Button;
import planeswalkers.simulatheure.ui.gui.EditionButtons;

/**
 *
 * @author Jean-Benoît
 */
public abstract class PopUpBox extends Box<String, Box> {

    private Color titleColor;
    private final PopUpTexts texts;
    protected double popUpHeight = 80;
    private final EditionButtons editionButtons;
    private Viewport colorBar;
    private Color colorBarColor = INVISIBLE;
    private double desiredHeight = 300;
    private double desiredWidth = 350;
    private final int buttonOffset = 190;

    /**
     * Is used to display text and buttons mixed on an horizontal line, the
     * buttons MUST be selected after the creation with the appropriate
     * function. It is important to note that the button are already contained
     * in the PopUpBox and are ready to serve.
     *
     * @param title The title to be displayed on the top left corner of the
     * PopUpBox, if need be
     * @param x
     * @param y
     */
    public PopUpBox(String title, double x, double y) {
        super(title, x, y);
        verticalPadding = 15;
        horizontalPadding = 25;
        texts = new PopUpTexts();
        editionButtons = new EditionButtons(relativeX, relativeY);
        setButtonSize(100);
        elementShape = new Viewport(relativeX, relativeY);
        setBackgroundColor(new Color(58, 79, 170));
        addChild(texts);
        addChild(editionButtons);
    }

    /**
     * Openning the popup with this version will only set its position, letting
     * the size be automaticly set to desiredWidth and desiredHeight
     *
     * @param x
     * @param y
     */
    public final void open(double x, double y) {
        setRenderProperties(new Viewport(x, y));
    }

    /**
     * This function sets the desired width and height and opens the popup at
     * the x, y position of the viewport.
     *
     * @param viewport
     */
    public final void open(Viewport viewport) {
        desiredWidth = viewport.getWidth();
        desiredHeight = viewport.getHeight();
        setRenderProperties(viewport);
    }

    /**
     * Hides a popus off the view, if you prefer not to close it.
     */
    public final void close() {
        setRenderProperties(new Viewport(-1000, -1000));
    }

    public void showTitle() {
        titleColor = Color.BLACK;
    }

    public void hideTitle() {
        titleColor = INVISIBLE;
    }

    public void addText(Text textToAdd) {
        texts.addChild(textToAdd);
    }

    public void removeText(Text textToAdd) {
        texts.removeChild(textToAdd);
    }

    public void addButton(Button buttonToAdd) {
        editionButtons.addButton(buttonToAdd);
    }

    public void removeButton(Button buttonToAdd) {
        editionButtons.removeChild(buttonToAdd);
    }

    @Override
    public void setRenderProperties(Viewport viewport) {

        relativeX = viewport.getX();
        relativeY = viewport.getY();
        desiredWidth = viewport.getWidth();
        desiredHeight = viewport.getHeight();

        double editionButtonsX = relativeX + desiredWidth - editionButtons.getBoxWidth() - horizontalPadding;
        editionButtons.resize(new Viewport(editionButtonsX, 1, 1, 1));

        elementShape = new Viewport(relativeX, relativeY);
        texts.setRenderProperties(elementShape);
        elementShape = new Viewport(relativeX - horizontalPadding, relativeY - verticalPadding, desiredWidth, desiredHeight);
        colorBar = new Viewport(relativeX, relativeY + desiredHeight / 3, 0.9 * desiredWidth, desiredHeight / 2.25);

    }

    @Override
    public void paint(Graphics2D graphics2D) {
        saveGraphicsConfigurations(graphics2D);

        graphics2D.setColor(getBackgroundColour());
        graphics2D.fill(elementShape.getShape());
        graphics2D.setColor(colorBarColor);
        graphics2D.fill(colorBar.getShape());
        for (Box b : getContainedElement().values()) {
            b.paint(graphics2D);
        }
        restoreGraphicsConfigurations(graphics2D);
    }

    @Override
    public double getBoxHeight() {
        double boxHeight = (2 * verticalPadding);
        double textHeight = texts.getBoxHeight();
        double buttonHeight = editionButtons.getBoxHeight();
        double totalHeight = Math.max(textHeight, buttonHeight) + boxHeight;
        return totalHeight;
    }

    @Override
    public double getBoxWidth() {
        double totalWidth = (2 * horizontalPadding);
        double textWidth = texts.getElementShape().getWidth();
        double buttonWidth = editionButtons.getBoxWidth();
        totalWidth += textWidth + buttonWidth;
        return totalWidth;
    }

    public final void setButtonSize(double buttonSize) {
        editionButtons.setButtonSize(buttonSize);
    }

    private class PopUpTexts extends TextBox {

        private final int defaultTextHeight = 20;

        public PopUpTexts() {
            super(PopUpTexts.class.getSimpleName(), 0, 0, defaultTextOffsets);
        }

        @Override
        public void setRenderProperties(Viewport viewport) {
            super.setRenderProperties(new Viewport(viewport.getX(), viewport.getY() + getBoxHeight() - defaultTextHeight));
        }

        @Override
        public double getBoxHeight() {
            return super.getContainedElement().size() * defaultTextHeight;
        }

    }

    public void setColorBarColor(Color colorBarColor) {
        this.colorBarColor = colorBarColor;
    }
}
