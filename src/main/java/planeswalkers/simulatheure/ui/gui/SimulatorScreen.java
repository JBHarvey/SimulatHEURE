/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author Marc-Antoine Fortier
 */
public class SimulatorScreen {

    private final SimulationBox simulationBox;
    private final JFrame screen;
    private final Renderer renderer;

    protected static final int WIDTH = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
    protected static final int HEIGHT = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;

    /**
     * SimulatorScreen constructor, creates SImulatorScreen objects.
     *
     * @param simulationBox
     */
    public SimulatorScreen(SimulationBox simulationBox) {

        this.simulationBox = simulationBox;

        screen = new JFrame();
        initializeScreen();

        renderer = new Renderer(screen.getInsets(), simulationBox);

        addRendererToScreen();

    }

    private void initializeScreen() {
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setTitle("SimulatHEURE");
        screen.setLocation(0, 0);
        screen.setExtendedState(JFrame.MAXIMIZED_BOTH);
        screen.setUndecorated(true);
        screen.setSize(WIDTH, HEIGHT);
        screen.setIconImage(new ImageIcon("assets/LoGLOO.png").getImage());
        screen.setVisible(true);
        screen.setResizable(false);
        screen.setIgnoreRepaint(true);
    }

    private void addRendererToScreen() {
        screen.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        screen.add(renderer);
        screen.pack();
    }

    /**
     *
     */
    public final void startDisplay() {
        renderer.setBufferStrategy(screen);
        renderer.run();
    }

    /**
     * Return the application's JFrame.
     *
     * @return screen the application's JFrame.
     */
    public JFrame getScreen() {
        return screen;
    }

    /**
     *
     * @return
     */
    public Renderer getRenderer() {
        return renderer;
    }

}
