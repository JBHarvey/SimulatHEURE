/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and unfold the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import java.awt.Color;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.ChannelDown;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.ChannelUp;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.Earth;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.OpenFile;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.Quit;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.Redo;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.Save;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.Undo;
import planeswalkers.simulatheure.ui.components.NetworkEditionBox;
import planeswalkers.simulatheure.ui.components.RectangularBox;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 *
 * @author Jean-Benoît
 */
public class TopMenu extends NetworkEditionBox {

    protected int buttonSize = 80;
    protected double barHeight;
    private final MenuBarButtons menuButtons;
    private final ChannelUpBar up;
    private final ChannelDownBar down;
    final double unitarySize = getBoxHeight() - 2.75 * verticalPadding;

    public TopMenu(double x, double y) {
        super(TopMenu.class.getSimpleName(), x, y);
        setFrameColor(new Color(0, 0, 0, 0.5f));
        setBackgroundColor(Color.DARK_GRAY);
        horizontalPadding = 5;
        menuButtons = new MenuBarButtons(relativeX, relativeY);
        barHeight = menuButtons.getBoxHeight();
        up = new ChannelUpBar(relativeX, relativeY + 6 * buttonSize);
        down = new ChannelDownBar(relativeX, relativeY);
        addChild(down);
        fold();
    }

    public void unfold() {
        removeChild(down);
        addChild(menuButtons);
        addChild(up);
        menuButtons.resize(new Viewport(relativeX, relativeY, 5, barHeight));
        up.resize(new Viewport(relativeX, relativeY, 5, barHeight));
        Viewport upShape = up.getElementShape();
        elementShape = new Viewport(relativeX, relativeY, upShape.getWidth(), barHeight + upShape.getHeight() - 7 * verticalPadding / 10);
    }

    public final void fold() {
        removeChild(menuButtons);
        removeChild(up);
        addChild(down);
        down.resize(new Viewport(relativeX, relativeY, 5, unitarySize));
        Viewport downShape = down.getElementShape();
        elementShape = new Viewport(downShape.getX(), downShape.getY(), downShape.getWidth(), downShape.getHeight() - 7 * verticalPadding / 10);
    }

    private class MenuBarButtons extends RectangularBox {

        private final int buttonNumber = 6;

        public MenuBarButtons(double x, double y) {
            super(MenuBarButtons.class.getSimpleName(), x, y);
            addButton(new OpenFile());
            addButton(new Save());
            addButton(new Undo());
            addButton(new Redo());
            addButton(new Earth());
            addButton(new Quit());
            setBackgroundColor(Color.DARK_GRAY);
            setFrameColor(new Color(0, 0, 0, 0.5f));
        }

        @Override
        public double getBoxHeight() {
            return buttonNumber * buttonSize;
        }

    }

    private class ChannelUpBar extends RectangularBox {

        private final ChannelUp up;

        public ChannelUpBar(double x, double y) {
            super(ChannelUpBar.class.getSimpleName(), x, y);
            setBackgroundColor(Color.DARK_GRAY);
            setFrameColor(new Color(1, 1, 1, 0.5f));
            up = new ChannelUp();
            addChild(up);

        }
    }

    private class ChannelDownBar extends RectangularBox {

        private final ChannelDown down;

        public ChannelDownBar(double x, double y) {
            super(ChannelDownBar.class.getSimpleName(), x, y);
            setBackgroundColor(Color.DARK_GRAY);
            setFrameColor(Color.LIGHT_GRAY);
            down = new ChannelDown();
            addChild(down);

        }
    }
}
