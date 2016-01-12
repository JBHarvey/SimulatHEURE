/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.units;

import java.io.Serializable;

/**
 * Modelisation of a triangular distribution.
 *
 * @author Antoine
 */
public class TriangularDistribution implements Serializable{

    private final int minimum;
    private final int mean;
    private final int maximum;
    private int computedValue;

    /**
     *
     * @param min the min time value
     * @param mean the average time value
     * @param max the max time value
     */
    public TriangularDistribution(int min, int mean, int max) {
        minimum = min;
        this.mean = mean;
        maximum = max;
    }

    public void reset() {
        computedValue = 0;
    }

    public int fetchComputation() {
        if (computedValue == 0) {
            compute();
        }
        return computedValue;
    }

    /**
     * Compute a random value that follows a triangular distribution.
     */
    private void compute() {
        float distributionSpan = maximum - minimum;
        float beforeAverage = mean - minimum;
        float afterAverage = maximum - mean;
        double ratioUnderAverage = beforeAverage / distributionSpan;
        double drawResult = Math.random();
        if (drawResult < ratioUnderAverage) {
            computedValue = (int) (minimum +  Math.sqrt(drawResult * distributionSpan * beforeAverage));
        } else {
            computedValue = (int) (maximum -  Math.sqrt((1 - drawResult) * distributionSpan * afterAverage));
        }
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getMean() {
        return mean;
    }
}
