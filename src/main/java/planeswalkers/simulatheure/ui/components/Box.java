/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * A Box is a composite Display element which is stored in the shape of a tree.
 * It's deepest parent (the root) is the one that contains all the others. The
 * leaves are the highest child, which contain no element. They can be box or
 * not. But it they are not boxes, they cannot be anything more then leaves
 *
 * This structures allows to place the different Display element in an order
 * that eases the navigation between them and the passing of the different
 * Events that may occur in the program's course.
 *
 * Each Displayable can have only one parent, but each Box can have many
 * children.
 *
 * @author Jean-Benoit Harvey
 * @param <S>
 * @param <D>
 */
public abstract class Box<S, D extends Displayable> extends Displayable {

    protected final Map<String, D> containedElement;
    protected double horizontalPadding;
    protected double verticalPadding;
    Color backgroundColour, frameColor;

    /**
     * ContainerComponent Constructor, creates a ContainerComponent object.
     *
     * @param name The name of the ContainerComponent.
     * @param x
     * @param y
     */
    public Box(String name, double x, double y) {
        super(name);
        containedElement = Collections.synchronizedMap(new LinkedHashMap<String, D>());
        relativeX = x;
        relativeY = y;
        parent = null;
        frameColor = INVISIBLE;
    }

    /**
     * Will return the highest child of this Box. that is the child that is the
     * most nested in other boxes which all intersect or contain the Viewport
     * passed in parameters
     *
     * @param viewport The mouse position when the MouseEvent occurred, that the
     * highest child of this Box is in contact with
     * @return the child at the highest nested level.
     */
    @Override
    public Displayable findHighestClick(Viewport viewport) {
        Displayable highest = this;
        ListIterator<String> iter
                = new ArrayList<>(containedElement.keySet()).listIterator(containedElement.size());

        while (iter.hasPrevious()) {
            String elementName = iter.previous();
            if (containedElement.get(elementName).elementShape.intersects(viewport.getBounds2D())) {
                return containedElement.get(elementName).findHighestClick(viewport);

            }
        }

        return highest;
    }

    /**
     * Will return the first child of this Box. that is the child that is the
     * most nested in other boxes which all intersect or contain the Viewport
     * passed in parameters
     *
     * @param viewport The mouse position when the MouseEvent occurred, that the
     * highest child of this Box is in contact with
     * @return the first child clicked on
     */
    public Displayable findDirectClick(Viewport viewport) {
        Displayable highest = this;
        ListIterator<String> iter
                = new ArrayList<>(containedElement.keySet()).listIterator(containedElement.size());

        while (iter.hasPrevious()) {
            String elementName = iter.previous();
            if (containedElement.get(elementName).elementShape.intersects(viewport.getBounds2D())) {
                return containedElement.get(elementName);

            }
        }
        return highest;
    }

    /**
     * Adds a displayable element to its children, or replace it if it was
     * already there. Those elements will receive the MouseEvents received by
     * their containing element if they are subject to, that is if they are
     * located at the mouse position at the creation of the MouseEven. They will
     * receive KeyEvents only is they have a focused state
     *
     * @param <D>
     * @param displayable The Displayable to set as child in the box
     */
    public void addChild(D displayable) {
        displayable.parent = this;
        containedElement.put(displayable.getName(), displayable);

    }

    /**
     * Will add every element not already present in its children. Those
     * elements will receive the MouseEvents received by their containing
     * element if they are subject to, that is if they are located at the mouse
     * position at the creation of the MouseEven. They will receive KeyEvents
     * only is they have a focused state
     *
     * @param displayables The ArrayList of Displayable to add in the Box
     */
    public void addTheseChildren(ArrayList<D> displayables) {
        for (D displayable : displayables) {
            addChild(displayable);
        }
    }

    /**
     * Will remove the Displayable element if it is a child of the called Box.
     * THIS ACTION WILL ALSO VOID THE ELEMENT'S PARENT.
     *
     * @param displayable the child to remove
     */
    public void removeChild(D displayable) {
        displayable.parent = null;
        containedElement.remove(displayable.name);

    }

    /**
     * Will remove every element from the ArrayList present in its children.
     * THIS ACTION WILL ALSO VOID THE ELEMENTS' PARENT.
     *
     * @param displayables
     */
    public void removeTheseChildren(ArrayList<D> displayables) {
        for (D displayable : displayables) {
            removeChild(displayable);
        }
    }

    /**
     * Will remove every child of this instance THIS ACTION WILL ALSO VOID THE
     * ELEMENTS' PARENT.
     *
     */
    public void removeAllChildren() {
        containedElement.clear();
    }

    /**
     * Gives access to the contained elements of the Box
     *
     * @return the contained Display elements ArrayList
     */
    public Map<String, D> getContainedElement() {
        return containedElement;
    }

    /**
     * This method converts the entered time into int, which represent the
     * number of minutes from midnight
     *
     * @return
     */
    protected int textToSeconds(String secondInText) {
        return LocalTime.parse(secondInText).toSecondOfDay();
    }

    /**
     * This method converts the entered time into int, which represent the
     * number of minutes from midnight
     *
     * @return
     */
    protected String secondsToText(int timeInSeconds) {
        return LocalTime.MIDNIGHT.plusSeconds(timeInSeconds).format(DateTimeFormatter.ISO_TIME);
    }

    /**
     *
     * @param graphics2D
     */
    @Override
    public abstract void paint(Graphics2D graphics2D);

    /**
     *
     * @param viewport
     */
    @Override
    public abstract void setRenderProperties(planeswalkers.simulatheure.ui.components.Viewport viewport);

    @Override
    void displayName(Graphics2D graphics2D) {
    }

    public boolean containtsSomethings() {
        return !getContainedElement().isEmpty();
    }

    /**
     *
     * @param colour
     */
    public void setBackgroundColor(Color colour) {
        this.backgroundColour = colour;
    }

    public void setFrameColor(Color frameColor) {
        this.frameColor = frameColor;
    }

    public D getChild(String name) {
        return containedElement.get(name);
    }

    public Color getBackgroundColour() {
        return backgroundColour;
    }

    public Color getFrameColor() {
        return frameColor;
    }

    public double getBoxHeight() {
        return elementShape.getHeight();
    }

    public double getBoxWidth() {
        return elementShape.getWidth();
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

}
