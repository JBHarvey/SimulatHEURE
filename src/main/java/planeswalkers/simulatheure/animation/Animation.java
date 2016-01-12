/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;

/**
 *
 * @author Jean-Benoît & Antoine
 */
public class Animation {

    private HashMap<Integer, Integer> segmentTransitTime;
    private HashMap<Integer, Integer> circuitTransitTime;
    private HashMap<Integer, ArrayList<Bus>> circuitBus;
    private int startTime;
    private int endTime;
    
    //Passenger Simlation
    private ArrayList<Integer> eventTimes;
    private HashMap<Integer, ArrayList<Integer>> busEnterTimes;
    private HashMap<Integer, ArrayList<Integer>> busExitTimes;
    private HashMap<Integer, ArrayList<Integer>> stationEnterTimes;
    private HashMap<Integer, ArrayList<Integer>> stationExitTimes;

    public Animation() {
    }

    void setCircuits(HashMap<Integer, Integer> circuitTransitTime) {
        this.circuitTransitTime = circuitTransitTime;
    }

    void setBus(HashMap<Integer, ArrayList<Bus>> circuitBus) {
        this.circuitBus = circuitBus;
    }

    void setSegments(HashMap<Integer, Integer> segmentTransitTime) {
        this.segmentTransitTime = segmentTransitTime;
    }

    void setTimes(int start, int end) {
        startTime = start;
        endTime = end;
    }

    public void setEventTimes(ArrayList<Integer> eventTimes) {
        this.eventTimes = eventTimes;
    }

    public void setBusEnterTimes(HashMap<Integer, ArrayList<Integer>> busEnterTimes) {
        this.busEnterTimes = busEnterTimes;
    }

    public void setBusExitTimes(HashMap<Integer, ArrayList<Integer>> busExitTimes) {
        this.busExitTimes = busExitTimes;
    }

    public void setStationEnterTimes(HashMap<Integer, ArrayList<Integer>> stationEnterTimes) {
        this.stationEnterTimes = stationEnterTimes;
    }

    public void setStationExitTimes(HashMap<Integer, ArrayList<Integer>> stationExitTimes) {
        this.stationExitTimes = stationExitTimes;
    }

    public HashMap<Integer, Integer> getSegmentTransitTime() {
        return segmentTransitTime;
    }

    public HashMap<Integer, Integer> getCircuitTransitTime() {
        return circuitTransitTime;
    }

    public HashMap<Integer, ArrayList<Bus>> getCircuitBus() {
        return circuitBus;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public ArrayList<Integer> getEventTimes() {
        return eventTimes;
    }

    public HashMap<Integer, ArrayList<Integer>> getBusEnterTimes() {
        return busEnterTimes;
    }

    public HashMap<Integer, ArrayList<Integer>> getBusExitTimes() {
        return busExitTimes;
    }

    public HashMap<Integer, ArrayList<Integer>> getStationEnterTimes() {
        return stationEnterTimes;
    }

    public HashMap<Integer, ArrayList<Integer>> getStationExitTimes() {
        return stationExitTimes;
    }
}
