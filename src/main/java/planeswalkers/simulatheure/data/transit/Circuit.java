/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.transit;

import java.util.ArrayList;
import planeswalkers.simulatheure.data.units.TriangularDistribution;

/**
 *
 * @author Daniel
 */
public class Circuit extends Transit {

    protected final ArrayList<Integer> path;
    private final int busCapacity;
    private boolean loop;
    private int maxBusNumber;

    /**
     * Constructor creation of circuit with maxBusNumber speficied
     *
     * @param name
     * @param path
     * @param firstSpawnTime
     * @param busCapacity
     * @param isLoop
     * @param maxBusNumber
     * @param spawnDistribution
     */
    public Circuit(String name, ArrayList<Integer> path, int firstSpawnTime,
            int busCapacity, boolean isLoop, int maxBusNumber, TriangularDistribution spawnDistribution) {
        super(name, firstSpawnTime, spawnDistribution);
        this.path = path;
        this.busCapacity = busCapacity;
        this.loop = isLoop;
        this.maxBusNumber = maxBusNumber;
    }

    /**
     * Constructor modification of circuit with maxBusNumber Specified
     *
     * @param name
     * @param identifier
     * @param path
     * @param firstSpawnTime
     * @param busCapacity
     * @param isLoop
     * @param maxBusNumber
     * @param spawnDistribution
     */
    public Circuit(String name, int identifier, ArrayList<Integer> path, int firstSpawnTime,
            int busCapacity, boolean isLoop, int maxBusNumber, TriangularDistribution spawnDistribution) {
        super(name, identifier, firstSpawnTime, spawnDistribution);
        this.path = path;
        this.busCapacity = busCapacity;
        this.loop = isLoop;
        this.maxBusNumber = maxBusNumber;
    }

    public int getBusCapacity() {
        return busCapacity;
    }

    public boolean isLoop() {
        return loop;
    }

    public boolean isBusMaximumReached(int currentNumberOfBus) {
        return currentNumberOfBus == maxBusNumber;
    }

    public ArrayList<Integer> getPath() {
        return path;
    }

    public int getMaxBusNumber() {
        return maxBusNumber;
    }
}
