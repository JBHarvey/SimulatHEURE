/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Jean-Benoît
 */
public class AnimationClock {

    Timer timer;
    private int numberOfDays;
    private double speedStep = 1;
    private final long CHRONO_WAIT = 16;
    private double animationTime;
    boolean running = false;

    public void AnimationClock() {
        setAnimationTime(0);
        numberOfDays = 0;
    }

    public void start() {
        timer = new Timer();
        running = true;
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                animationTime += speedStep;
            }

        }, 0, (long) (CHRONO_WAIT));
    }

    public void stop() {
        if (running) {
            timer.cancel();
            running = false;
        }
    }

    public void setSpeedStep(double newStep) {
        speedStep = newStep;
    }

    public void setAnimationTime(int secondsAfterMidnight) {
        animationTime = secondsAfterMidnight;
    }

    public int getAnimationTime() {
        return (int) animationTime;
    }

    @Override
    public String toString() {
        return LocalTime.MIDNIGHT.plusSeconds((int) animationTime).format(DateTimeFormatter.ISO_TIME);
    }

}
