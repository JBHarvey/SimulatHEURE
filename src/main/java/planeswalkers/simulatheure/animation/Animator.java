/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.animation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import planeswalkers.simulatheure.data.SimulationElement;
import planeswalkers.simulatheure.data.networkelement.Segment;
import planeswalkers.simulatheure.data.networkelement.Station;
import planeswalkers.simulatheure.data.transit.Circuit;
import planeswalkers.simulatheure.data.transit.CircuitFraction;
import planeswalkers.simulatheure.data.transit.TransportNeed;

/**
 *
 * @author Jean-Benoît & Antoine
 */
public class Animator {

    public static final int ONE_DAY_IN_SECONDS = 86400;
    public static final int SECONDS_IN_ONE_HOUR = 3600;

    private final String SEGMENT = Segment.class.getSimpleName();
    private final String STATION = Station.class.getSimpleName();
    private final String CIRCUIT = Circuit.class.getSimpleName();
    private final String TRANSPORT_NEED = TransportNeed.class.getSimpleName();

    private final ArrayList<Animation> animations;
    private Report report;
    private Animation currentAnimation;
    private HashMap<String, HashMap<Integer, SimulationElement>> network;
    private HashMap<Integer, Integer> segmentTransitTime;
    private HashMap<Integer, Integer> circuitTransitTime;
    private HashMap<Integer, ArrayList<Integer>> segmentTimesPerCircuit;
    private HashMap<Integer, ArrayList<Bus>> circuitBus;
    private ArrayList<HashMap<String, ArrayList<Integer>>> reportData;
    private int startTime = 5 * SECONDS_IN_ONE_HOUR;
    private int endTime = 1 * SECONDS_IN_ONE_HOUR + ONE_DAY_IN_SECONDS;
    private int numberOfReplications = 1;

    //Passenger Simlation
    private ArrayList<Integer> eventTimes;
    private HashMap<Integer, ArrayList<Integer>> busEnterTimes;
    private HashMap<Integer, ArrayList<Integer>> busExitTimes;
    private HashMap<Integer, ArrayList<Integer>> stationEnterTimes;
    private HashMap<Integer, ArrayList<Integer>> stationExitTimes;
    private int lastBusID;

    public Animator() {
        animations = new ArrayList<>();
    }

    /**
     *
     * @param start time in seconds since midnight
     * @param end time in seconds since midnight
     * @param replications
     */
    public void parametrize(int start, int end, int replications) {
        startTime = start;
        endTime = end;
        numberOfReplications = replications;
        if (endTime <= startTime) {
            endTime += ONE_DAY_IN_SECONDS;
        }
    }

    public void initAnimator(HashMap<String, HashMap<Integer, SimulationElement>> networkElements) {
        network = networkElements;
    }

    public void computeAnimation() {
        animations.clear();
        reportData = new ArrayList<>();
        for (int i = 0; i < numberOfReplications; ++i) {
            animations.add(computeReplication());
        }
        report = new Report(reportData);
    }

    private Animation computeReplication() {
        currentAnimation = new Animation();
        initSegmentTransitTimes();
        placeBusOnCircuits();

        eventTimes = new ArrayList<>();
        busEnterTimes = new HashMap<>();
        busExitTimes = new HashMap<>();
        stationEnterTimes = new HashMap<>();
        stationExitTimes = new HashMap<>();

        for (ArrayList<Bus> busList : circuitBus.values()) {
            for (Bus bus : busList) {
                busEnterTimes.put(bus.getIdentifier(), new ArrayList<Integer>());
                busExitTimes.put(bus.getIdentifier(), new ArrayList<Integer>());
            }
        }

        for (SimulationElement element : network.get(STATION).values()) {
            Station station = (Station) element;
            stationEnterTimes.put(station.getIdentifier(), new ArrayList<Integer>());
            stationExitTimes.put(station.getIdentifier(), new ArrayList<Integer>());
        }

        placePassengerInNetwork();

        Collections.sort(eventTimes);
        for (ArrayList<Integer> list : busEnterTimes.values()) {
            Collections.sort(list);
        }
        for (ArrayList<Integer> list : busExitTimes.values()) {
            Collections.sort(list);
        }
        for (ArrayList<Integer> list : stationEnterTimes.values()) {
            Collections.sort(list);
        }
        for (ArrayList<Integer> list : stationExitTimes.values()) {
            Collections.sort(list);
        }

        currentAnimation.setEventTimes(eventTimes);
        currentAnimation.setBusEnterTimes(busEnterTimes);
        currentAnimation.setBusExitTimes(busExitTimes);
        currentAnimation.setStationEnterTimes(stationEnterTimes);
        currentAnimation.setStationExitTimes(stationExitTimes);
        currentAnimation.setSegments(segmentTransitTime);
        currentAnimation.setCircuits(circuitTransitTime);
        currentAnimation.setBus(circuitBus);
        currentAnimation.setTimes(startTime, endTime);

        return currentAnimation;
    }

    private void initSegmentTransitTimes() {
        segmentTransitTime = new HashMap<>();
        circuitTransitTime = new HashMap<>();
        segmentTimesPerCircuit = new HashMap<>();

        for (SimulationElement element : network.get(SEGMENT).values()) {
            Segment segment = (Segment) element;
            segment.resetTransitTime();
            segmentTransitTime.put(segment.getIdentifier(), segment.fetchTransitTime());
        }
    }

    private void initCircuitTransitTime(Circuit circuit) {
        int transitTime = 0;
        ArrayList<Integer> transitTimes = new ArrayList<>();
        transitTimes.add(transitTime);
        for (Integer segmentID : circuit.getPath()) {
            transitTime += segmentTransitTime.get(segmentID);
            transitTimes.add(transitTime);
        }
        segmentTimesPerCircuit.put(circuit.getIdentifier(), transitTimes);
        circuitTransitTime.put(circuit.getIdentifier(), transitTime);
    }

    private void placeBusOnCircuits() {
        circuitBus = new HashMap<>();
        ArrayList<Bus> bus;
        int birthTime, lifeTime;
        for (SimulationElement element : network.get(CIRCUIT).values()) {
            Circuit circuit = (Circuit) element;
            bus = new ArrayList<>();
            initCircuitTransitTime(circuit);
            birthTime = circuit.getFirstSpawnTime();
            if (circuit.isLoop()) {
                while (!circuit.isBusMaximumReached(bus.size())) {
                    bus.add(new Bus(birthTime, endTime));
                    birthTime += circuit.drawSpawn();
                }
            } else {
                lifeTime = circuitTransitTime.get(circuit.getIdentifier());
                while (birthTime < endTime) {
                    bus.add(new Bus(birthTime, computeBusDeath(birthTime, lifeTime)));
                    birthTime += circuit.drawSpawn();
                }
            }
            circuitBus.put(circuit.getIdentifier(), bus);
        }
    }

    private void placePassengerInNetwork() {
        int birthTime, deathTime;
        //ArrayList<Passenger> passengers;
        HashMap<String, ArrayList<Integer>> replicationData = new HashMap<>();
        for (SimulationElement element : network.get(TRANSPORT_NEED).values()) {
            ArrayList<Integer> lifeTimesData = new ArrayList<>();
            TransportNeed transportNeed = (TransportNeed) element;
            //passengers = new ArrayList<>();
            birthTime = transportNeed.getFirstSpawnTime();
            while (birthTime < endTime) {
                lastBusID = 0;
                deathTime = computePassengerDeath(transportNeed, birthTime);
                //passengers.add(new Passenger(birthTime, deathTime));
                if (deathTime < endTime) {
                    //System.out.println("Death Time  : " + deathTime);
                    lifeTimesData.add(deathTime - birthTime);
                }
                birthTime += transportNeed.drawSpawn();
            }
            replicationData.put(transportNeed.getName(), lifeTimesData);
        }
        reportData.add(replicationData);
    }

    private int computeBusDeath(int birthTime, int lifeTime) {
        int deathTime;
        deathTime = birthTime + lifeTime;

        // Semble inutile...
        if (deathTime > endTime) {
            deathTime = endTime;
        }
        return deathTime;
    }

    private int computePassengerDeath(TransportNeed transportNeed, int birthTime) {
        int waitingTime, transitTime, circuitTime;
        int lifeTime = 0;
        int simulationTime = birthTime;
        ArrayList<CircuitFraction> fractions = transportNeed.getCircuitFractions();
        LinkedHashMap<Integer, ArrayList<Integer>> path = new LinkedHashMap<>();
        for (CircuitFraction fraction : fractions) {
            path.put(fraction.getCircuitID(), fraction.getSegments());
        }

        for (Iterator<Integer> it = path.keySet().iterator(); it.hasNext();) {
            Integer circuitID = it.next();
            waitingTime = getWaitingTime(simulationTime, circuitID, path.get(circuitID).get(0));
            if (waitingTime == endTime) {
                return endTime;
            }
            transitTime = getTransitTime(path.get(circuitID));
            circuitTime = waitingTime + transitTime;
            lifeTime += circuitTime;
            simulationTime += circuitTime;
        }

        if (lastBusID != 0) {
            eventTimes.add(simulationTime);
            busExitTimes.get(lastBusID).add(simulationTime);
        }

        return birthTime + lifeTime;
    }

    private int getWaitingTime(int simulationTime, int circuitID, int segmentID) {

        Circuit circuit = (Circuit) network.get(CIRCUIT).get(circuitID);
        Segment segment = (Segment) network.get(SEGMENT).get(segmentID);
        ArrayList<Integer> stationEntries = new ArrayList<>();

        ArrayList<Integer> path = circuit.getPath();
        int timeToSegment = 0;
        int busTimeToSegment;
        for (int i = 0; i < path.indexOf(segmentID); i++) {
            timeToSegment += segmentTransitTime.get(path.get(i));
        }

        ArrayList<Bus> theBus = circuitBus.get(circuitID);

        eventTimes.add(simulationTime);

        if (stationEnterTimes.get(segment.getOrigin()) == null) {
            stationEnterTimes.put(segment.getOrigin(), stationEntries);
        }
        stationEnterTimes.get(segment.getOrigin()).add(simulationTime);

        if (lastBusID != 0) {
            busExitTimes.get(lastBusID).add(simulationTime);
        }

        // Impossible to reach number
        int arrivalTime = Integer.MAX_VALUE;

        if (circuit.isLoop()) {
            int laps;
            int progression;
            int nextLapTime;
            int busID = -1;
            for (Bus bus : theBus) {
                laps = (simulationTime - bus.getBirthTime()) / circuitTransitTime.get(circuitID);
                progression = simulationTime - bus.getBirthTime() - (laps * circuitTransitTime.get(circuitID));
                if (progression <= timeToSegment) {
                    busTimeToSegment = simulationTime + (timeToSegment - progression);
                } else {
                    nextLapTime = circuitTransitTime.get(circuitID) - progression;
                    busTimeToSegment = simulationTime + timeToSegment + nextLapTime;
                }
                if (busTimeToSegment < arrivalTime) {
                    arrivalTime = busTimeToSegment;
                    busID = bus.getIdentifier();
                }
            }
            eventTimes.add(arrivalTime);
            busEnterTimes.get(busID).add(arrivalTime);
            lastBusID = busID;
            stationExitTimes.get(segment.getOrigin()).add(arrivalTime);
            return arrivalTime - simulationTime;
        } else {
            for (Bus bus : theBus) {
                if (bus.getDeathTime() >= simulationTime) {
                    busTimeToSegment = bus.getBirthTime() + timeToSegment;
                    if (busTimeToSegment >= simulationTime && busTimeToSegment < arrivalTime) {
                        arrivalTime = busTimeToSegment;
                        eventTimes.add(arrivalTime);
                        busEnterTimes.get(bus.getIdentifier()).add(arrivalTime);
                        lastBusID = bus.getIdentifier();
                        stationExitTimes.get(segment.getOrigin()).add(arrivalTime);
                        return arrivalTime - simulationTime;
                    }
                }
            }
        }

        return endTime;
    }

    private int getTransitTime(ArrayList<Integer> path) {
        int transitTime = 0;
        for (Integer segmentID : path) {
            transitTime += segmentTransitTime.get(segmentID);
        }

        return transitTime;
    }

    public ArrayList<Animation> getAnimations() {
        return animations;
    }

    public Report getReport() {
        return report;
    }
}
