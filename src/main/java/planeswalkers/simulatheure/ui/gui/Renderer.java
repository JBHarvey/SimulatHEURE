/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import planeswalkers.simulatheure.ui.components.Displayable;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 *
 * @author Marc-Antoine Fortier
 */
public class Renderer extends JPanel {

    private static final int ONE_MILLISEC_IN_NANOSEC = 1000000;
    private static final int ONE_SEC_IN_MILLISEC = 1000;
    private final int FPS = 30;
    private Insets insets;
    private SimulationBox simulationBox;

    private Graphics graphics;
    private Graphics2D graphics2D;
    private BufferStrategy bufferStrategy;
    private BufferedImage bufferedImage;
    private GraphicsConfiguration graphicsConfig;
    private ArrayList<Displayable> elementsToDisplay;

    private long renderingTime;
    private int cumulativeTime = 0;
    private int frameCounter = 0;

    private int FPS_WAIT_TIME;

    /**
     * Renderer constructor, creates renderer objects.
     *
     * @param insets
     * @param simulationBox
     */
    public Renderer(Insets insets, SimulationBox simulationBox) {
        this.insets = insets;
        this.simulationBox = simulationBox;

        initRenderingValues();
        prepareDesignAreaCanvas();
        fetchGraphicsConfiguration();
        createBackBuffer();
    }

    private void initRenderingValues() {

        graphics = null;
        graphics2D = null;
        FPS_WAIT_TIME = ONE_SEC_IN_MILLISEC / FPS;
    }

    private void prepareDesignAreaCanvas() {
        this.setIgnoreRepaint(true);
        if (!Toolkit.getDefaultToolkit().toString().contains("sun.awt.X11.XToolkit") && !Toolkit.getDefaultToolkit().toString().contains("sun.awt.windows.WToolkit")) {
            insets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        }
        if (Toolkit.getDefaultToolkit().toString().contains("sun.awt.windows.WToolkit")) {
            this.setPreferredSize(new Dimension(SimulatorScreen.WIDTH - insets.left - insets.right, SimulatorScreen.HEIGHT - insets.top - 2 * insets.bottom));
        } else {
            this.setPreferredSize(new Dimension(SimulatorScreen.WIDTH - insets.left - insets.right, SimulatorScreen.HEIGHT - insets.top - insets.bottom));
        }
    }

    private void fetchGraphicsConfiguration() throws HeadlessException {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        graphicsConfig = graphicsDevice.getDefaultConfiguration();
    }

    /**
     * Generate an image compatible for hardware acceleration.
     */
    private void createBackBuffer() {
        bufferedImage = graphicsConfig.createCompatibleImage(SimulatorScreen.WIDTH, SimulatorScreen.HEIGHT);
    }

    /**
     * Execute the renderer, makes the application running.
     */
    public void run() {

        int actualFPS = 0;
        long currentTime;

        while (true) {

            try {

                currentTime = System.nanoTime();

                resetCanvasToPrintNextFrame();

                AffineTransform oldTransform = graphics2D.getTransform();
                simulationBox.paint(graphics2D);
                graphics2D.setTransform(oldTransform);

                displayFPS(actualFPS);

                displayFrame();

                actualFPS = controlFPS(currentTime, actualFPS);

            } catch (InterruptedException ex) {
                Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {

                disposeOfGraphicRessourses();

            }
        }
    }

    /**
     *
     * @param frame
     */
    public void setBufferStrategy(JFrame frame) {
        frame.createBufferStrategy(2);
        do {
            bufferStrategy = frame.getBufferStrategy();
        } while (bufferStrategy == null);
    }

    private void resetCanvasToPrintNextFrame() {
        graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        if (Toolkit.getDefaultToolkit().toString().contains("sun.awt.windows.WToolkit")) {
            graphics2D.translate(0, insets.top - insets.bottom);
        } else {
            graphics2D.translate(insets.left, insets.top);
        }
        graphics2D.setColor(Color.GRAY);
        graphics2D.fillRect(0, 0, SimulatorScreen.WIDTH - insets.left - insets.right - 1, SimulatorScreen.HEIGHT - insets.bottom - insets.top - 1);
    }

    private void displayFPS(int actualFPS) {
        graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(String.format("FPS: %s", actualFPS), 5, SimulatorScreen.HEIGHT - 8);
    }

    private void displayFrame() {

        graphics = bufferStrategy.getDrawGraphics();
        graphics.drawImage(bufferedImage, 0, 0, null);
        // Security in case of lost of the image in memory (VolatileImage).
        if (!bufferStrategy.contentsLost()) {
            bufferStrategy.show();
        }
    }

    private int controlFPS(long currentTime, int actualFPS) throws InterruptedException {
        renderingTime = (System.nanoTime() - currentTime) / ONE_MILLISEC_IN_NANOSEC;

        //This line is the one that can throw the exception
        Thread.sleep(Math.max(0, FPS_WAIT_TIME - renderingTime));

        renderingTime = (System.nanoTime() - currentTime) / ONE_MILLISEC_IN_NANOSEC;
        cumulativeTime += renderingTime;
        // Count FPS rate.
        if (cumulativeTime > ONE_SEC_IN_MILLISEC) {
            cumulativeTime -= ONE_SEC_IN_MILLISEC;
            actualFPS = frameCounter;
            frameCounter = 0;
        }
        ++frameCounter;
        return actualFPS;
    }

    private void disposeOfGraphicRessourses() {
        if (graphics != null) {
            graphics.dispose();
        }
        if (graphics2D != null) {
            graphics2D.dispose();
        }
    }

    /**
     *
     * @param viewport
     * @return
     */
    public Displayable findHighestClick(Viewport viewport) {
        return elementsToDisplay.get(elementsToDisplay.size() - 1).findHighestClick(viewport);
    }
}
