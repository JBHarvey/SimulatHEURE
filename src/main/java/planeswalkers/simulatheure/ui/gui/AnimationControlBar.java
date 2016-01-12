/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ListIterator;
import planeswalkers.simulatheure.ui.components.Box;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Forward;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.NextReplication;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Pause;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Play;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.PreviousReplication;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Rabbit;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Restart;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Rewind;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Stop;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Turtle;
import planeswalkers.simulatheure.ui.components.Buttons.CircularButton;
import planeswalkers.simulatheure.ui.components.CircularBox;
import planeswalkers.simulatheure.ui.components.Displayable;
import static planeswalkers.simulatheure.ui.components.Displayable.INVISIBLE;
import planeswalkers.simulatheure.ui.components.RectangularBox;
import planeswalkers.simulatheure.ui.components.Text;
import planeswalkers.simulatheure.ui.components.TextBox;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 *
 * @author Jean-Benoît
 */
public class AnimationControlBar extends Box<Double, Displayable> {

    private final Color mainColor = new Color(0, 124, 216);
    private final Color textColor = new Color(242, 242, 242);
    private final Color backgroundColor = Color.BLACK;
    private static final double MARGIN = 40;
    private final int TEXT_SIZE = 80;
    private final double horizontalSize = SimulatorScreen.WIDTH;
    private final double middle = 3 * horizontalSize / 8;
    private final double right = 4 * horizontalSize / 7;
    private static final double verticalLimit = SimulatorScreen.HEIGHT;
    private final double BUTTON_Y = relativeY + 3 * MARGIN / 2;
    private final ProgressBar progressBar;
    private final TimeBox timeBox;
    private final SpeedControl speedControl;
    private final AnimationButtons animationButtons;

    public AnimationControlBar(double x, double y) {
        super(AnimationControlBar.class.getSimpleName(), x, verticalLimit - 5 * MARGIN - 4);
        progressBar = new ProgressBar(name, new Viewport(x + MARGIN / 2, relativeY + MARGIN / 3, horizontalSize - 2 * MARGIN, 2 * MARGIN / 3));
        timeBox = new TimeBox(relativeY + 2 * MARGIN);
        animationButtons = new AnimationButtons(horizontalSize / 3 - MARGIN / 2, BUTTON_Y, (middle));
        speedControl = new SpeedControl(x + (right), BUTTON_Y);
        addChild(progressBar);
        addChild(timeBox);
        addChild(animationButtons);
        addChild(speedControl);
        setRenderProperties(elementShape);
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        saveGraphicsConfigurations(graphics2D);
        graphics2D.setColor(backgroundColor);
        graphics2D.fill(elementShape.getShape());
        int size = getContainedElement().size();
        ListIterator<Displayable> iter = new ArrayList<>(getContainedElement().values()).listIterator(size);
        while (iter.hasPrevious()) {
            iter.previous().paint(graphics2D);
        }

        restoreGraphicsConfigurations(graphics2D);
    }

    @Override
    public void setRenderProperties(Viewport viewport) {
        double progressBarHeight = 3 * MARGIN / 2;
        double buttonHeight = animationButtons.getBoxHeight();
        double totalHeight = progressBarHeight + MARGIN + buttonHeight;
        elementShape = new Viewport(relativeX, relativeY, horizontalSize, totalHeight);
    }

    public void resetTime(double start, double end) {
        progressBar.setBorders(start, end);
        timeBox.setTime((int) start);
        speedControl.reset();
    }

    public double getProgressionFromClickAt(double absoluteX) {
        return progressBar.getProgressionFromClickAt(absoluteX);
    }

    public void play() {
        animationButtons.play();
    }

    public void pause() {
        animationButtons.pause();
    }

    public final void setTime(int timeInSeconds) {
        progressBar.setProgressionValue(timeInSeconds);
        timeBox.setTime(timeInSeconds);
    }

    public double faster() {
        return speedControl.faster();
    }

    public double slower() {
        return speedControl.slower();
    }

    private class TimeBox extends TextBox {

        private final Text time;

        public TimeBox(double y) {
            super(TimeBox.class.getSimpleName(), 0, y, new Viewport(MARGIN, MARGIN, 15, 0));
            time = new Text("Time");
            time.setTitle("");
            time.setSeparator("");
            time.notEditable();
            time.setBackground(INVISIBLE);
            time.setFontSize(TEXT_SIZE);
            time.setContentColor(textColor);
            setTime(0);
            addChild(time);
            setRenderProperties(new Viewport(relativeX, relativeY));
        }

        public final void setTime(int timeInSeconds) {
            time.setContent(LocalTime.MIDNIGHT.plusSeconds(timeInSeconds).toString());
        }

    }

    private class AnimationButtons extends RectangularBox {

        private final CircularButton restart, rewind, play, pause, stop, forward, next, previous;
        private double middleSize;

        public AnimationButtons(double x, double y, double width) {
            super(AnimationButtons.class.getSimpleName(), x, y);
            middleSize = width;
            restart = new Restart();
            rewind = new Rewind();
            play = new Play();
            pause = new Pause();
            stop = new Stop();
            forward = new Forward();
            next = new NextReplication();
            previous = new PreviousReplication();
            setFrameColor(INVISIBLE);
            setBackgroundColor(INVISIBLE);
            addButton(previous);
            addButton(rewind);
            addButton(restart);
            resize(new Viewport(relativeX, relativeY, middleSize, 5));
        }

        public void pause() {
            removeLastButtons();
            removeStatusButton();
            addButton(play);
            addLastButtons();
            resize(new Viewport(relativeX, relativeY, middleSize, 5));
        }

        public void play() {
            removeLastButtons();
            removeStatusButton();
            addButton(pause);
            addLastButtons();
            resize(new Viewport(relativeX, relativeY, middleSize, 5));
        }

        private void removeStatusButton() {
            removeIfPresent(play);
            removeIfPresent(pause);
        }

        private void addLastButtons() {
            addButton(stop);
            addButton(forward);
            addButton(next);
        }

        private void removeLastButtons() {
            removeIfPresent(next);
            removeIfPresent(forward);
            removeIfPresent(stop);
        }

        private void removeIfPresent(CircularButton button) {
            if (getContainedElement().containsValue(button)) {
                removeChild(button);
            }
        }

    }

    private class SpeedControl extends Box<String, Box> {

        private final int ELEMENT_SEPARATION = 25;
        private final int SIZE = 100;
        private final RabbitBox faster;
        private final TurtleBox slower;
        private final SpeedControl.Speeds speeds;

        public SpeedControl(double x, double y) {
            super(SpeedControl.class.getSimpleName(), x, y);

            slower = new TurtleBox();
            slower.setRenderProperties(new Viewport(x + 2 * SIZE, y + (SIZE) / 2));

            speeds = new Speeds(x + 1.8 * SIZE, y + 0.2 * SIZE);

            faster = new RabbitBox();
            faster.setRenderProperties(new Viewport(x + 6 * SIZE, y + (SIZE) / 2));

            addChild(slower);
            addChild(speeds);
            addChild(faster);
            elementShape = new Viewport(x + SIZE, y, 3 * horizontalSize, 3 * SIZE);
        }

        @Override
        public void paint(Graphics2D graphics2D) {
            saveGraphicsConfigurations(graphics2D);

            int size = getContainedElement().size();
            ListIterator<Box> iter = new ArrayList<>(getContainedElement().values()).listIterator(size);
            while (iter.hasPrevious()) {
                iter.previous().paint(graphics2D);
            }
            restoreGraphicsConfigurations(graphics2D);
        }

        @Override
        public void setRenderProperties(Viewport viewport) {
        }

        public void reset() {
            speeds.reset();
        }

        public double faster() {
            return speeds.faster();
        }

        public double slower() {
            return speeds.slower();
        }

        private class RabbitBox extends CircularBox {

            public RabbitBox() {
                super(RabbitBox.class.getSimpleName());
                addButton(new Rabbit());
                setFrameColor(INVISIBLE);
                setBackgroundColor(INVISIBLE);
            }
        }

        private class TurtleBox extends CircularBox {

            public TurtleBox() {
                super(TurtleBox.class.getSimpleName());
                addButton(new Turtle());
                setFrameColor(INVISIBLE);
                setBackgroundColor(INVISIBLE);
            }
        }

        private class Speeds extends Box<Integer, Displayable> {

            private double speedIndex = 32;
            private final double barSize = 22;
            private final double barGap = barSize / 7.5;
            private final double maxHeight = SIZE;
            private final double maxValue = Math.pow(2, 7);

            public Speeds(double x, double y) {
                super(Speeds.class.getSimpleName(), x, y);
                elementShape = new Viewport(x, y, 1, 1);
                setBackgroundColor(INVISIBLE);
                setFrameColor(INVISIBLE);

            }

            public void reset() {
                speedIndex = 32;
            }

            public double slower() {
                if (speedIndex / 32 > 512) {
                    speedIndex = 512 * 32;
                } else if (speedIndex > 1) {
                    speedIndex = speedIndex / 2;
                } else {
                    speedIndex = 0;
                }
                return speedIndex / 32;
            }

            private double faster() {
                if (speedIndex == 0) {
                    speedIndex++;
                } else if (speedIndex / 32 == 512) {
                    speedIndex = Double.MAX_VALUE * 2;
                } else if (speedIndex < Double.MAX_VALUE) {
                    speedIndex = speedIndex * 2;
                }
                return speedIndex / 32;
            }

            @Override
            public void paint(Graphics2D graphics2D) {
                saveGraphicsConfigurations(graphics2D);
                graphics2D.setColor(mainColor);
                double currentHeight, currentValue, ratio;
                double barY, barX = relativeX;
                for (int i = 0; Math.pow(2, i) <= speedIndex && i < 16; i++) {
                    if (i == 15) {
                        graphics2D.setColor(Color.RED.darker());
                    }
                    currentValue = Math.pow(2, i + 1);
                    ratio = currentValue / maxValue;
                    currentHeight = Math.sqrt(ratio * maxHeight);
                    currentHeight = Math.min(currentHeight, 1.5 * SIZE);

                    barY = relativeY + maxHeight - currentHeight;
                    graphics2D.fillRect((int) barX, (int) barY, (int) barSize, (int) currentHeight);
                    barX += (barSize + barGap);
                }
                restoreGraphicsConfigurations(graphics2D);
            }

            @Override
            public void setRenderProperties(Viewport viewport
            ) {
            }
        }
    }
}
