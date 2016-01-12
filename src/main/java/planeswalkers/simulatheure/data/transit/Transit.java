/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.transit;

import planeswalkers.simulatheure.data.SimulationElement;
import planeswalkers.simulatheure.data.units.TriangularDistribution;

/**
 *
 * @author Daniel
 */
public abstract class Transit extends SimulationElement {

    protected final TriangularDistribution spawnDistribution;
    protected int firstSpawnTime;

    /**
     * Transit Constructor
     *
     * @param name
     * @param firstSpawnTime
     * @param distribution
     */
    public Transit(String name, int firstSpawnTime, TriangularDistribution distribution) {
        super(name);
        spawnDistribution = distribution;
        this.firstSpawnTime = firstSpawnTime;
    }

    public Transit(String name, int identifier, int firstSpawnTime, TriangularDistribution distribution) {
        super(name, identifier);
        spawnDistribution = distribution;
        this.firstSpawnTime = firstSpawnTime;
    }

    public int drawSpawn() {
        spawnDistribution.reset();
        return spawnDistribution.fetchComputation();
    }

    public int getFirstSpawnTime() {
        return firstSpawnTime;
    }

    public TriangularDistribution getSpawnDistribution() {
        return spawnDistribution;
    }
}
