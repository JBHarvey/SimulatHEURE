/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import java.awt.Color;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.AnimationParameters;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.StartAnimation;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.ChannelLeft;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.ChannelRight;
import planeswalkers.simulatheure.ui.components.NetworkEditionBox;
import planeswalkers.simulatheure.ui.components.RectangularBox;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 *
 * @author Jean-Benoît
 */
public class AnimationTools extends NetworkEditionBox {

    protected int buttonSize = 80;
    protected double barHeight;
    private final AnimationToolsButtons animationButtons;
    private final ChannelRightBar right;
    private final ChannelLeftBar left;

    /**
     *
     * @param x the top right corner of the bar
     * @param y top right corner
     */
    public AnimationTools(double x, double y) {
        super(AnimationTools.class.getSimpleName(), x, y);
        setFrameColor(new Color(0, 0, 0, 0.5f));
        verticalPadding = 5;
        relativeX -= (buttonSize + 3 * verticalPadding);
        animationButtons = new AnimationToolsButtons(relativeX, relativeY);
        barHeight = animationButtons.getBoxHeight();
        right = new ChannelRightBar(relativeX - buttonSize, relativeY + buttonSize / 2);
        left = new ChannelLeftBar(relativeX, relativeY + buttonSize / 2);
        addChild(left);
        fold();
    }

    public void unfold() {
        removeChild(left);
        addChild(animationButtons);
        addChild(right);
        animationButtons.resize(new Viewport(relativeX, relativeY, 5, barHeight));
        right.resize(new Viewport(relativeX, relativeY, 5, buttonSize * 2));
        elementShape = new Viewport(relativeX - (4 * buttonSize / 5), relativeY, getBoxWidth() - 2.75 * horizontalPadding, barHeight);
        setFrameColor(new Color(0, 0, 0, 0.5f));
//        setBackgroundColor(Color.DARK_GRAY);
    }

    public final void fold() {
        removeChild(animationButtons);
        removeChild(right);
        addChild(left);
        left.resize(new Viewport(relativeX, relativeY, 5, buttonSize * 2));
        elementShape = new Viewport(relativeX + (1 * buttonSize / 5), relativeY, getBoxWidth() - 2.75 * horizontalPadding, barHeight);
        setFrameColor(INVISIBLE);
        setBackgroundColor(INVISIBLE);
    }

    private class AnimationToolsButtons extends RectangularBox {

        private final int buttonNumber = 2;

        public AnimationToolsButtons(double x, double y) {
            super(AnimationToolsButtons.class.getSimpleName(), x, y);
            addButton(new AnimationParameters());
            addButton(new StartAnimation());
            setBackgroundColor(Color.DARK_GRAY);
            setFrameColor(new Color(0, 0, 0, 0.5f));
        }

        @Override
        public double getBoxHeight() {
            return buttonNumber * buttonSize;
        }

    }

    private class ChannelRightBar extends RectangularBox {

        private final ChannelRight right;

        public ChannelRightBar(double x, double y) {
            super(ChannelRightBar.class.getSimpleName(), x, y);
            setBackgroundColor(Color.DARK_GRAY);
            setFrameColor(new Color(1, 1, 1, 0.5f));
            right = new ChannelRight();
            addChild(right);

        }
    }

    private class ChannelLeftBar extends RectangularBox {

        private final ChannelLeft left;

        public ChannelLeftBar(double x, double y) {
            super(ChannelLeftBar.class.getSimpleName(), x, y);
            setBackgroundColor(Color.DARK_GRAY);
            setFrameColor(Color.LIGHT_GRAY);

            left = new ChannelLeft();
            addChild(left);

        }
    }
}
