/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui;

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;
import com.kitfox.svg.ShapeElement;
import java.awt.Shape;
import java.util.HashMap;

/**
 * Asset Manager singleton class, deals with all of the extrinsic information of
 * simulator elements, be it transit, network or window components
 *
 * @author Jean-Benoit
 */
public class AssetManager {

    private final HashMap<String, SVGDiagram> diagrams;
    private final HashMap<String, Shape> shapes;
    private final SVGUniverse svgUniverse;

    private AssetManager() {
        diagrams = new HashMap<>();
        shapes = new HashMap<>();
        svgUniverse = new SVGUniverse();
    }

    /**
     * Method that return the single existing instance of the AssetManager.
     *
     * @return the only existing instance of Asset Manager
     */
    public static AssetManager getInstance() {
        return AssetManagerHolder.INSTANCE;
    }

    /**
     * Method that manages the intrinsic data of the simulator elements. It
     * contains it's image, behavioral methods and state classes.
     *
     * @param componentClassName the name of the class of the required
     * simulation component.
     * @return the SVGDiagram that correspond to the requested element.
     */
    public SVGDiagram getSimulationComponent(String componentClassName) {
        loadDiagramIfNecessary(componentClassName);
        loadShapeIfNecessary(componentClassName);
        return diagrams.get(componentClassName);
    }

    /**
     *
     * @param componentClassName
     * @return
     */
    public SVGDiagram getUIToolComponent(String componentClassName) {
        loadDiagramIfNecessary(componentClassName);
        return diagrams.get(componentClassName);

    }

    private void loadDiagramIfNecessary(String componentClassName) {
        if (!diagrams.containsKey(componentClassName)) {
            SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(getClass().getResource("/" + componentClassName + ".svg")));
            diagrams.put(componentClassName, diagram);
        }
    }

    private void loadShapeIfNecessary(String componentClassName) {
        SVGDiagram diagram = diagrams.get(componentClassName);
        ShapeElement shape = (ShapeElement) diagram.getRoot().getChild("shape");
        shapes.put(componentClassName, shape.getShape());
    }

    /**
     *
     * @param name
     * @return
     */
    public Shape getShape(String name) {
        loadDiagramIfNecessary(name);
        loadShapeIfNecessary(name);
        return shapes.get(name);
    }

    /**
     *
     * @return
     */
    public SVGDiagram getSimulationComponent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static class AssetManagerHolder {

        private static final AssetManager INSTANCE = new AssetManager();
    }
}
