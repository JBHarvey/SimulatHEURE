/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

import planeswalkers.simulatheure.data.units.TriangularDistribution;

/**
 *
 * @author Jean-Benoît
 */
public class TriangularDistributionBox extends TextBox {

    private final Text minimum;
    private final Text meanTime;
    private final Text maximum;
    private final String UNITS = "minutes";
    private final int CONVERSION = 60;

    public TriangularDistributionBox(String name) {
        super(name + TriangularDistributionBox.class.getSimpleName(), 0, 0, defaultTextOffsets);
        minimum = new Text("Minimum");
        meanTime = new Text("Moyenne");
        maximum = new Text("Maximum");
        minimum.setUnits(UNITS);
        meanTime.setUnits(UNITS);
        maximum.setUnits(UNITS);

        addChild(minimum);
        addChild(meanTime);
        addChild(maximum);
        horizontalOffset = 0;
    }

    public void open(TriangularDistribution triangularDistribution) {
        minimum.setContent(String.valueOf(triangularDistribution.getMinimum() / CONVERSION));
        meanTime.setContent(String.valueOf(triangularDistribution.getMean() / CONVERSION));
        maximum.setContent(String.valueOf(triangularDistribution.getMaximum() / CONVERSION));
    }

    public void setMinimum(String min) {
        minimum.setContent(min);
    }

    public void setAverage(String mean) {
        meanTime.setContent(mean);
    }

    public void setMaximum(String max) {
        maximum.setContent(max);
    }

    public TriangularDistribution getDistribution() {
        int min = CONVERSION * Integer.parseInt(minimum.getContent());
        int mean = CONVERSION * Integer.parseInt(meanTime.getContent());
        int max = CONVERSION * Integer.parseInt(maximum.getContent());
        return new TriangularDistribution(min, mean, max);
    }

}
