/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import planeswalkers.simulatheure.animation.Animation;
import planeswalkers.simulatheure.animation.Bus;
import planeswalkers.simulatheure.animation.Report;
import planeswalkers.simulatheure.ui.gui.AnimationClock;
import planeswalkers.simulatheure.ui.gui.AnimationControlBar;

/**
 *
 * @author Marc-Antoine Fortier
 */
public class AnimatorImage extends Box<String, BusImage> {

    private final int FULL_DAY_IN_SECONDS = 86400;
    public static final int ONE_HOUR_IN_SECONDS = 3600;

    private final String BUS = Bus.class.getSimpleName();

    private Animation currentAnimation;
    private int currentReplication = 0;
    private int eventNumber = 0;
    private AnimationClock clock;
    private int startingTime = ONE_HOUR_IN_SECONDS * 5;
    private int endTime = ONE_HOUR_IN_SECONDS + FULL_DAY_IN_SECONDS;
    private int numberOfReplication = 1;
    private int currentTime;
    private double simulationSpeed;
    private boolean animating = false;
    private boolean paused;

    private ArrayList<Animation> animations;
    private Report report;
    private ArrayList<CircuitImage> circuits;
    private ArrayList<StationImage> stations;
    private HashMap<Integer, SegmentImage> segments;
    private final HashMap<Bus, BusImage> busImages;
    private final ArrayList<BusImage> currentlyDisplayedBus;

    private AnimationControlBar controlBar;
    private boolean animationOver;

    public AnimatorImage() {
        super(AnimatorImage.class.getSimpleName(), 0, 0);
        busImages = new HashMap<>();
        currentlyDisplayedBus = new ArrayList<>();
    }

    public void initAnimation(ArrayList<Animation> animations, Report report, HashMap<Integer, SegmentImage> segmentsForAnimation,
            ArrayList<CircuitImage> circuitImages, ArrayList<StationImage> stationImages) {
        this.animations = animations;
        this.report = report;
        segments = segmentsForAnimation;
        circuits = circuitImages;
        stations = stationImages;
        numberOfReplication = animations.size();
    }

    public void startAnimation(AnimationControlBar bar) {
        controlBar = bar;
        clock = new AnimationClock();
        currentReplication = -1;
        nextReplication();
        animationOver = false;
        animating = true;
    }

    public void nextReplication() {
        eventNumber = 0;
        currentReplication++;
        if (currentReplication < numberOfReplication) {
            startReplication();
        } else {
            stopAnimation();
            currentReplication = 0;
            animationOver = true;
        }
    }

    public String getReport() {
        String stringReport = " ";
        if (animationOver) {
            stringReport = report.toString();
        }
        return stringReport;
    }

    public void previousReplication() {
        if (currentReplication > 0) {
            currentReplication--;
        }
        startReplication();
    }

    private void startReplication() {
        eventNumber = 0;
        currentAnimation = animations.get(currentReplication);
        startingTime = currentAnimation.getStartTime();
        endTime = currentAnimation.getEndTime();
        resetStationsNumberOfPassenger();
        setSegmentTransitTime();
        initBus();
        clock.setAnimationTime(startingTime);
        controlBar.resetTime(startingTime, endTime);
        resetStationsNumberOfPassenger();
        controlBar.setTime(0);
        controlBar.pause();
        paused = true;

    }

    public void pause() {
        clock.stop();
        controlBar.pause();
        paused = true;
    }

    public void forwardAnHour() {
        if (endTime - currentTime < ONE_HOUR_IN_SECONDS) {
            setTime(endTime);
        } else {
            setTime(currentTime + ONE_HOUR_IN_SECONDS);
        }
    }

    public void backAnHour() {
        if (currentTime > ONE_HOUR_IN_SECONDS) {
            setTime(currentTime - ONE_HOUR_IN_SECONDS);
        } else {
            restartReplication();
        }
    }

    public void restartReplication() {
        setTime(startingTime);

    }

    public void setTime(int newTime) {
        clock.stop();
        currentTime = newTime;
        clock.setAnimationTime(currentTime);

        clock.start();
    }

    public void play() {
        clock.start();
        controlBar.play();
        paused = false;
    }

    public void faster() {
        double fasterStep = controlBar.faster();
        clock.setSpeedStep(fasterStep);
    }

    public void slower() {
        double slowerStep = controlBar.slower();
        clock.setSpeedStep(slowerStep);
    }

    public void stopAnimation() {
        resetStationsNumberOfPassenger();
        pause();
        animating = false;
    }

    private void resetStationsNumberOfPassenger() {
        for (StationImage station : stations) {
            station.setNumberOfpassenger(0);
        }
    }

    private void setSegmentTransitTime() {
        for (Integer segmentID : currentAnimation.getSegmentTransitTime().keySet()) {
            if (segments.containsKey(segmentID)) {
                if (segments.get(segmentID).getIdentifier() == segmentID) {
                    segments.get(segmentID).setTransitTime(currentAnimation.getSegmentTransitTime().get(segmentID), segmentID);
                } else if (segments.get(segmentID).isTwoWay() && segments.get(segmentID).getSecondWayId() == segmentID) {
                    int secondWayID = segments.get(segmentID).getSecondWayId();
                    segments.get(segmentID).setTransitTime(currentAnimation.getSegmentTransitTime().get(segmentID), segmentID);
                }
            }
        }
    }

    private void initBus() {
        busImages.clear();
        currentlyDisplayedBus.clear();
        for (CircuitImage circuit : circuits) {
            for (Bus bus : currentAnimation.getCircuitBus().get(circuit.getIdentifier())) {
                BusImage busImage = new BusImage(circuit.getDisplayName(), bus.getIdentifier());
                busImage.setColor(circuit.getColor());
                busImages.put(bus, busImage);
            }
        }
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        saveGraphicsConfigurations(graphics2D);
        int size = getContainedElement().size();
        ListIterator<BusImage> iter = new ArrayList<>(getContainedElement().values()).listIterator(size);
        while (iter.hasPrevious()) {
            EntityImage image = iter.previous();
            image.paint(graphics2D);
        }
        controlBar.paint(graphics2D);
        restoreGraphicsConfigurations(graphics2D);

    }

    public void setRenderProperties() {

        placeBusImages();
        placePassengers();
        currentTime = clock.getAnimationTime();
        controlBar.setTime(currentTime);

    }

    private void placePassengers() {
        currentTime = clock.getAnimationTime();
        if (!currentAnimation.getEventTimes().isEmpty() && currentAnimation.getEventTimes().get(eventNumber) <= currentTime) {
            eventNumber++;
            for (Integer busID : currentAnimation.getBusEnterTimes().keySet()) {
                if (!currentAnimation.getBusEnterTimes().get(busID).isEmpty()) {
                    int time = currentAnimation.getBusEnterTimes().get(busID).get(0);
                    if (time <= currentTime) {
                        currentAnimation.getBusEnterTimes().get(busID).remove(0);
                        for (Bus bus : busImages.keySet()) {
                            if (busID == bus.getIdentifier()) {
                                bus.setNumberOfpassenger(bus.getNumberOfPassenger() + 1);
                            }
                        }
                    }
                }
            }

            for (Integer stationID : currentAnimation.getStationExitTimes().keySet()) {
                if (!currentAnimation.getStationExitTimes().get(stationID).isEmpty()) {
                    int time = currentAnimation.getStationExitTimes().get(stationID).get(0);
                    if (time <= currentTime) {
                        currentAnimation.getStationExitTimes().get(stationID).remove(0);
                        for (StationImage station : stations) {
                            if (stationID == station.getIdentifier()) {
                                station.setNumberOfpassenger(station.getNumberOfpassenger() - 1);
                            }
                        }
                    }
                }
            }

            for (Integer stationID : currentAnimation.getStationEnterTimes().keySet()) {
                if (!currentAnimation.getStationEnterTimes().get(stationID).isEmpty()) {
                    int time = currentAnimation.getStationEnterTimes().get(stationID).get(0);
                    if (time <= currentTime) {
                        currentAnimation.getStationEnterTimes().get(stationID).remove(0);
                        for (StationImage station : stations) {
                            if (stationID == station.getIdentifier()) {
                                station.setNumberOfpassenger(station.getNumberOfpassenger() + 1);
                            }
                        }
                    }
                }
            }

            for (Integer busID : currentAnimation.getBusExitTimes().keySet()) {
                if (!currentAnimation.getBusExitTimes().get(busID).isEmpty()) {
                    int time = currentAnimation.getBusExitTimes().get(busID).get(0);
                    if (time <= currentTime) {
                        currentAnimation.getBusExitTimes().get(busID).remove(0);
                        for (Bus bus : busImages.keySet()) {
                            if (busID == bus.getIdentifier()) {
                                bus.setNumberOfpassenger(bus.getNumberOfPassenger() - 1);
                            }
                        }
                    }
                }
            }
        }
    }

    private void placeBusImages() {
        currentTime = clock.getAnimationTime();
        currentlyDisplayedBus.clear();
        if (currentTime < endTime) {
            int timeOnCircuit;
            int circuitTransitTime;
            BusImage currentBusImage;
            if (circuits != null) {
                for (CircuitImage circuit : circuits) {
                    int circuitID = circuit.getIdentifier();
                    for (Bus bus : currentAnimation.getCircuitBus().get(circuitID)) {
                        if (bus.isAlive(currentTime)) {
                            currentBusImage = busImages.get(bus);
                            timeOnCircuit = currentTime - bus.getBirthTime();
                            circuitTransitTime = currentAnimation.getCircuitTransitTime().get(circuitID);
                            while (timeOnCircuit > circuitTransitTime) {
                                timeOnCircuit -= circuitTransitTime;
                            }
                            ArrayList<Object> returnedValues = circuit.getCurrentSegment(timeOnCircuit);
                            SegmentImage segment = (SegmentImage) returnedValues.get(0);
                            boolean reverse = (boolean) returnedValues.get(1);
                            currentBusImage.setRenderProperties(circuit.getProgressionOffLastBus(), bus.getNumberOfPassenger(), segment, reverse);
                            currentlyDisplayedBus.add(currentBusImage);
                        }
                    }
                }
            }
            removeAllChildren();
            addTheseChildren(currentlyDisplayedBus);
        } else {
            nextReplication();
        }
    }

    @Override
    public void setRenderProperties(Viewport viewport) {
    }

    public int getStartingTime() {
        return startingTime;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getNumberOfReplication() {
        return numberOfReplication;
    }

    public double getSimulationSpeed() {
        return simulationSpeed;
    }

    public void setStartingTime(int startingTime) {
        this.startingTime = startingTime;
    }

    public void setEndTime(int endTime) {
        if (endTime <= startingTime) {
            this.endTime = endTime + FULL_DAY_IN_SECONDS;
        } else {
            this.endTime = endTime;
        }
    }

    public void setNumberOfReplication(int numberOfReplication) {
        this.numberOfReplication = numberOfReplication;
    }

    public boolean isAnimating() {
        return animating;
    }

}
