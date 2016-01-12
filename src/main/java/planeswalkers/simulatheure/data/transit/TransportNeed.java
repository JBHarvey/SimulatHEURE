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
public class TransportNeed extends Transit {

    private final ArrayList<CircuitFraction> circuitFractions;

    /**
     *
     * @param name
     * @param circuitFractions
     * @param firstSpawnTime
     * @param spawnDistribution
     */
    public TransportNeed(String name, ArrayList<CircuitFraction> circuitFractions, int firstSpawnTime,
            TriangularDistribution spawnDistribution) {
        super(name, firstSpawnTime, spawnDistribution);
        this.circuitFractions = circuitFractions;
    }

    /**
     *
     * @param name
     * @param identifier
     * @param circuitFractions
     * @param firstSpawnTime
     * @param spawnDistribution
     */
    public TransportNeed(String name, int identifier, ArrayList<CircuitFraction> circuitFractions, int firstSpawnTime,
            TriangularDistribution spawnDistribution) {
        super(name, identifier, firstSpawnTime, spawnDistribution);
        this.circuitFractions = circuitFractions;
    }

    public ArrayList<CircuitFraction> getCircuitFractions() {
        return circuitFractions;
    }

}
