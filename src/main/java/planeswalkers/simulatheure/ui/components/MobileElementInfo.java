/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.components;

/**
 *
 * @author Jean-Benoît
 */
public class MobileElementInfo extends TextBox {

    private final Text firstApparition;
    private Text maximalCapacity;
    private Text loop;
    private Text busCapacity;
    private final String CIRCUIT = CircuitImage.class.getSimpleName();

    public MobileElementInfo(TransitImage transit) {
        super(transit.getDisplayName() + "BusControll", 0, 0, defaultTextOffsets);

        if (transit.getClass().getSimpleName().equals(CIRCUIT)) {
            CircuitImage circuitImage = (CircuitImage) transit;

            firstApparition = new Text("Heure du premier autobus");

            maximalCapacity = new Text("Capacité maximale");
            loop = new Text("Boucle");
            busCapacity = new Text("Capacité des autobus");

            maximalCapacity.setContent(String.valueOf(circuitImage.getMaxBusNumber()));
            loop.setContent(String.valueOf(circuitImage.isLoop()));
            busCapacity.setContent(String.valueOf(circuitImage.getBusCapacity()));

            addChild(maximalCapacity);
            addChild(loop);
            addChild(busCapacity);
        } else {
            firstApparition = new Text("Heure du premier passager");
        }
        horizontalOffset = 0;
        firstApparition.setContent(String.valueOf(secondsToText(transit.getFirstApparitionTime())));
        addChild(firstApparition);
    }

    public int getFirstApparition() {
        return textToSeconds(firstApparition.getContent());
    }

    public int getMaximalCapacity() {
        return Integer.parseInt(maximalCapacity.getContent());
    }

    public int getBusCapacity() {
        return Integer.parseInt(busCapacity.getContent());
    }

    public boolean isLoop() {
        return Boolean.parseBoolean(loop.getContent());
    }
}
