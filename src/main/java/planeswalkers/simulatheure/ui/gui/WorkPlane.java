/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import planeswalkers.simulatheure.data.SimulationElement;
import planeswalkers.simulatheure.data.networkelement.Intersection;
import planeswalkers.simulatheure.data.networkelement.Node;
import planeswalkers.simulatheure.data.networkelement.Segment;
import planeswalkers.simulatheure.data.networkelement.Station;
import planeswalkers.simulatheure.data.transit.Circuit;
import planeswalkers.simulatheure.data.transit.CircuitFraction;
import planeswalkers.simulatheure.data.transit.TransportNeed;
import planeswalkers.simulatheure.data.units.Coordinates;
import planeswalkers.simulatheure.data.units.TriangularDistribution;
import planeswalkers.simulatheure.ui.DataReceiver;
import planeswalkers.simulatheure.ui.components.AnimatorImage;
import planeswalkers.simulatheure.ui.components.Box;
import planeswalkers.simulatheure.ui.components.CircuitFractionImage;
import planeswalkers.simulatheure.ui.components.CircuitImage;
import planeswalkers.simulatheure.ui.components.Displayable;
import planeswalkers.simulatheure.ui.components.IntersectionImage;
import planeswalkers.simulatheure.ui.components.NodeImage;
import planeswalkers.simulatheure.ui.components.PathImage;
import planeswalkers.simulatheure.ui.components.SegmentImage;
import planeswalkers.simulatheure.ui.components.SimulationImage;
import planeswalkers.simulatheure.ui.components.StationImage;
import planeswalkers.simulatheure.ui.components.TransitImage;
import planeswalkers.simulatheure.ui.components.TransportNeedImage;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 * The UI representation of the work plane (formerly known as design plane). It
 * contains his simulation components, and has methods to paint itself and to
 * listen on it.
 *
 * @author Marc-Antoine Fortier
 */
public final class WorkPlane extends Box<String, SimulationImage> {

    public static final double PREFERRED_SIMULATION_COMPONENT_SCALE = 0.0000001;
    private final String SEGMENT_IMAGE = SegmentImage.class.getSimpleName();
    private final String STATION_IMAGE = StationImage.class.getSimpleName();
    private final String INTERSECTION_IMAGE = IntersectionImage.class.getSimpleName();
    private final String SEGMENT = Segment.class.getSimpleName();
    private final String STATION = Station.class.getSimpleName();
    private final String INTERSECTION = Intersection.class.getSimpleName();
    private final String CIRCUIT = Circuit.class.getSimpleName();
    private final String CIRCUIT_IMAGE = CircuitImage.class.getSimpleName();
    private final String TRANSPORT_NEED = TransportNeed.class.getSimpleName();
    private final String TRANSPORT_NEED_IMAGE = TransportNeedImage.class.getSimpleName();

    private ConcurrentHashMap<String, HashMap<Integer, SimulationElement>> currentElements;
    private ConcurrentHashMap<String, HashMap<Integer, SimulationElement>> newArrival;
    private final ConcurrentHashMap<Integer, Color> transitColorPalette;
    private ArrayList<NodeImage> nodesForCreation;
    private ArrayList<SegmentImage> segmentsForCreation;
    private ArrayList<SegmentImage> lockedForCreation;
    private final AnimatorImage animatorImage;

    private final HashMap<String, SimulationImage> UICursors;

    private static final double workPlaneXRatio = 1.0;
    private static final double workPlaneYRatio = 1.0;
    private Iterator it;

    private final Camera camera;
    private final DataReceiver dataReceiver;
    private boolean stateChanged = true;
    private boolean changedElement = false;
    private boolean modificationOnNode = false;
    private boolean modificationOnSegment = false;
    private boolean modificationOnTransit = false;
    private boolean displayMap = false;

    private BufferedImage map;

    /**
     * Constructs work plane objects.
     *
     * @param dataReceiver The data receiver to communicate with the model.
     */
    public WorkPlane(DataReceiver dataReceiver) {

        super(WorkPlane.class.getSimpleName(), (1.0 - workPlaneXRatio) * SimulatorScreen.WIDTH, (1.0 - workPlaneYRatio) * SimulatorScreen.HEIGHT);

        camera = new Camera(dataReceiver);

        initElementMaps();
        this.dataReceiver = dataReceiver;
        animatorImage = new AnimatorImage();

        elementShape = new Viewport(relativeX, relativeY, workPlaneXRatio * SimulatorScreen.WIDTH, workPlaneYRatio * SimulatorScreen.HEIGHT);

        transitColorPalette = new ConcurrentHashMap<>();

        UICursors = new HashMap<>();
        UICursors.put(STATION, new StationImage("CursorStation", -1, new Coordinates(0, 0, 0)));
        UICursors.put(INTERSECTION, new IntersectionImage("CursorItersection", -1, new Coordinates(0, 0, 0)));
        UICursors.put(SEGMENT, new SegmentImage("SegmentCursor", -1, new TriangularDistribution(5, 5, 5)));

        map = null;

        setFocusable(true);
        setVisible(true);
    }

    private void initElementMaps() {
        currentElements = new ConcurrentHashMap<>();
        newArrival = new ConcurrentHashMap<>();
        nodesForCreation = new ArrayList<>();
        segmentsForCreation = new ArrayList<>();
        lockedForCreation = new ArrayList<>();
        HashMap<Integer, SimulationElement> stations = new HashMap<>();
        HashMap<Integer, SimulationElement> intersections = new HashMap<>();
        HashMap<Integer, SimulationElement> segments = new HashMap<>();
        HashMap<Integer, SimulationElement> circuits = new HashMap<>();
        HashMap<Integer, SimulationElement> transportNeeds = new HashMap<>();
        currentElements.put(STATION, stations);
        currentElements.put(INTERSECTION, intersections);
        currentElements.put(SEGMENT, segments);
        currentElements.put(CIRCUIT, circuits);
        currentElements.put(TRANSPORT_NEED, transportNeeds);
    }

    /**
     * Paints the work plane on the Renderer Graphics2D object.
     *
     * @param graphics2D The Graphics2D object given by the Renderer to paint
     * the work plane on.
     */
    @Override
    public void paint(Graphics2D graphics2D) {
        saveGraphicsConfigurations(graphics2D);
        graphics2D.setClip(elementShape.getBounds2D());
        paintBackground(graphics2D);

        adjustChildrenIfNecessary();
        updateAnimation();

        paintSimulationImages(graphics2D);
        paintAnimation(graphics2D);

        restoreGraphicsConfigurations(graphics2D);
    }

    private void paintAnimation(Graphics2D graphics2D) {
        if (animatorImage.isAnimating()) {
            animatorImage.setRenderProperties();
            animatorImage.paint(graphics2D);
        }
    }

    private void paintBackground(Graphics2D graphics2D) {

        if (displayMap) {
            if (map == null) {
                try {
                    map = ImageIO.read(getClass().getResource("/QuebecCity.png"));
                } catch (IOException ex) {
                    Logger.getLogger(WorkPlane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            AffineTransform savedTransform = graphics2D.getTransform();
            AffineTransform transform1 = new AffineTransform();
            AffineTransform transform2 = new AffineTransform();
            Point2D anchor = (new Coordinates(46.7830306, -71.2769762, 0)).getXY();
            Coordinates offset = camera.getCoordinates().getDelta(new Coordinates(46.7830306, -71.2769762, 0));
            Point2D newPos = (new Coordinates(46.7830306 + offset.getLatitude(), -71.2769762 + offset.getLongitude(), 0)).getXY();
            Rectangle2D offsetRect = new Rectangle2D.Double(0, 0, newPos.getX() - anchor.getX(), newPos.getY() - anchor.getY());
            offsetRect = camera.transformOffsetBoundariesToWorkPlane(elementShape, offsetRect);
            transform1.translate(-(3214.0 - elementShape.getWidth() / 2.0), -(2889.0 - elementShape.getHeight() / 2.0));
            transform2.translate((int) offsetRect.getWidth(), (int) offsetRect.getHeight());
            transform2.scale(getZoomRatio() / 5.0, getZoomRatio() / 5.0);
            graphics2D.transform(transform2);
            graphics2D.transform(transform1);
            graphics2D.drawImage(map, null, null);
            graphics2D.setTransform(savedTransform);
        } else {
            graphics2D.setColor(Color.WHITE);
            graphics2D.fill(elementShape.getShape());
        }
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.draw(elementShape.getShape());

    }

    private void updateAnimation() {
        if (dataReceiver.isReceivedAnimation()) {
            animatorImage.initAnimation(dataReceiver.getAnimations(), dataReceiver.getReport(), getAllSegments(), getCircuitImages(), getAllStationImages());
        }
    }

    public void startAnimation(AnimationControlBar controlBar) {
        updateAnimation();
        animatorImage.startAnimation(controlBar);
    }

    public void pauseAnimation() {
        animatorImage.pause();
    }

    public void restartReplication() {
        animatorImage.restartReplication();
    }

    public void forwardAnHour() {
        animatorImage.forwardAnHour();
    }

    public void backAnHour() {
        animatorImage.backAnHour();
    }

    public void nextReplication() {
        animatorImage.nextReplication();
    }

    public void previousReplication() {
        animatorImage.previousReplication();
    }

    public void playAnimation() {
        animatorImage.play();
    }

    public void faster() {
        animatorImage.faster();
    }

    public void slower() {
        animatorImage.slower();
    }

    public void setClickedTime(double newTime) {
        animatorImage.setTime((int) newTime);
    }

    public void stopAnimation() {
        animatorImage.stopAnimation();
    }

    private void adjustChildrenIfNecessary() {
        newArrival = dataReceiver.getSimulationElements();
        updateAllStationImages();
        updateAllIntersectionImages();
        updateAllSegmentImages();
        updateAllCircuitImages();
        updateAllTransportNeedImages();
        changedElement = false;
    }

    /**
     *
     * @param elementType The name of the class of the SimulationElement
     * @return true if the list has changed, false otherwise.
     */
    private boolean elementsChanged(String elementType) {
        boolean updated = false;
        if (((currentElements.get(elementType).size() != newArrival.get(elementType).size() || changedElement)
                && (!modificationOnNode || elementType.equals(STATION) || elementType.equals(INTERSECTION) || elementType.equals(SEGMENT))) || modificationOnSegment
                || (changedElement && (elementType.equals(CIRCUIT) || elementType.equals(TRANSPORT_NEED)))) {
            removeDeletedElements(elementType);
            if ((elementType.equals(CIRCUIT) || elementType.equals(TRANSPORT_NEED)) && !modificationOnSegment) {
                modificationOnTransit = true;
                if (elementType.equals(TRANSPORT_NEED)) {
                    updateAllSegmentImages();
                    updateAllCircuitImages();
                }
                if (elementType.equals(CIRCUIT)) {
                    updateAllSegmentImages();
                    updateAllTransportNeedImages();
                }
                modificationOnTransit = false;
            }
            currentElements.get(elementType).clear();
            for (Integer ID : newArrival.get(elementType).keySet()) {
                currentElements.get(elementType).putIfAbsent(ID, newArrival.get(elementType).get(ID));
            }
            updated = true;
            stateChanged = true;
        }
        return updated;
    }

    private void removeDeletedElements(String elementType) {
        if (currentElements.get(elementType).size() > newArrival.get(elementType).size() || changedElement) {
            String id;
            int size = getContainedElement().size();
            ListIterator<String> iter = new ArrayList<>(getContainedElement().keySet()).listIterator(size);
            while (iter.hasPrevious()) {
                id = iter.previous();
                if (notInNewArrival(id, elementType) && elementIsOfType(id, elementType)) {
                    removeChild(getSimulationImage(id));
                    if (elementType.equals(SEGMENT)) {
                        modificationOnSegment = true;
                    }
                }
            }
            /**
             * Refresh transit display when a transit is removed.
             */
            if (elementType.equals(CIRCUIT) || elementType.equals(TRANSPORT_NEED)) {
                modificationOnNode = true;
                updateAllSegmentImages();
            }
            stateChanged = true;
        }
    }

    private void updateAllStationImages() {
        if (elementsChanged(STATION)) {
            it = currentElements.get(STATION).values().iterator();
            while (it.hasNext()) {
                updateStationImage((Node) it.next());
            }
            modificationOnNode = true;
        }
    }

    private void updateAllIntersectionImages() {
        if (elementsChanged(INTERSECTION)) {
            it = currentElements.get(INTERSECTION).values().iterator();
            while (it.hasNext()) {
                updateIntersectionImage((Node) it.next());
            }
            modificationOnNode = true;
        }
    }

    private void updateAllSegmentImages() {
        if (modificationOnTransit || elementsChanged(SEGMENT) || modificationOnNode) {
            it = currentElements.get(SEGMENT).values().iterator();
            while (it.hasNext()) {
                updateSegmentImage((Segment) it.next());
            }
        }
    }

    private void updateAllCircuitImages() {
        if (modificationOnTransit || elementsChanged(CIRCUIT) || modificationOnNode || modificationOnSegment) {
            it = currentElements.get(CIRCUIT).values().iterator();
            while (it.hasNext()) {
                updateCircuitImage((Circuit) it.next());
            }
        }
    }

    private void updateAllTransportNeedImages() {
        if (modificationOnTransit || elementsChanged(TRANSPORT_NEED) || modificationOnNode) {
            it = currentElements.get(TRANSPORT_NEED).values().iterator();
            while (it.hasNext()) {
                updateTransportNeedImage((TransportNeed) it.next());
            }
            if (modificationOnNode) {
                modificationOnNode = false;
            }
            if (modificationOnSegment) {
                modificationOnSegment = false;
            }
        }
    }

    private void updateStationImage(Node element) {
        StationImage stationImage = new StationImage(element.getName(), element.getIdentifier(), element.getCoordinates());
        addChild(stationImage);
    }

    private void updateIntersectionImage(Node element) {
        IntersectionImage intersectionImage = new IntersectionImage(element.getName(), element.getIdentifier(), element.getCoordinates());
        addChild(intersectionImage);
    }

    private void updateSegmentImage(Segment segment) {
        NodeImage nodeOrigin = (NodeImage) getChild(String.valueOf(segment.getOrigin()));
        NodeImage nodeEnd = (NodeImage) getChild(String.valueOf(segment.getEnd()));
        SegmentImage existingSegment = getSegmentImageFromNodeImages(nodeEnd, nodeOrigin);
        if (existingSegment != null) {
            if (segment.getIdentifier() == existingSegment.getIdentifier()) {
                existingSegment.setTriangularDistribution(segment.getTriangularDistribution());
            } else {
                existingSegment.setTwoWay(segment.getIdentifier(), segment.getTriangularDistribution());
            }
        } else {
            SegmentImage segmentImage = new SegmentImage(segment.getName(), segment.getIdentifier(), segment.getTriangularDistribution());
            segmentImage.setRenderProperties(nodeOrigin, nodeEnd);
            addChild(segmentImage);
        }
    }

    private void updateCircuitImage(Circuit circuit) {
        CircuitImage circuitImage = new CircuitImage(circuit);
        HashMap<Integer, SegmentImage> currentSegments = getAllSegments();
        ArrayList<PathImage> circuitSegments = new ArrayList<>();
        int last = -1;
        NodeImage nodeOrigin = null;
        NodeImage nodeEnd;
        for (Integer segmentID : circuit.getPath()) {
            SegmentImage segmentImage = currentSegments.get(segmentID);

            nodeOrigin = addOriginIfAbsent(nodeOrigin, currentSegments, segmentID);

            if (segmentID == segmentImage.getSecondWayId()) {
                segmentImage.addReverseTransit(circuitImage.getIdentifier());
            } else {
                segmentImage.addTransit(circuitImage.getIdentifier());
            }
            circuitSegments.add(new PathImage(segmentID, segmentImage));
            last = segmentID;
        }

        SegmentImage firstSegment = currentSegments.get(last);
        if (firstSegment.getIdentifier() == last) {
            nodeEnd = firstSegment.getEnd();
        } else {
            nodeEnd = firstSegment.getOrigin();
        }
        circuitImage.setOrigin(nodeOrigin);
        circuitImage.setEnd(nodeEnd);
        circuitImage.setSegments(circuitSegments);
        setTransitColor(circuitImage);
        addChild(circuitImage);

    }

    private void updateTransportNeedImage(TransportNeed transportNeed) {
        ArrayList<Integer> segmentsID = new ArrayList<>();
        HashMap<Integer, SegmentImage> currentSegments = getAllSegments();
        ArrayList<PathImage> transportNeedSegments = new ArrayList<>();
        ArrayList<CircuitFractionImage> transportNeedSegmentsOrganizedByCircuit = new ArrayList<>();
        int last = -1;
        NodeImage nodeOrigin = null;
        NodeImage nodeEnd;
        for (CircuitFraction circuitFraction : transportNeed.getCircuitFractions()) {
            ArrayList<PathImage> segmentImages = new ArrayList<>();
            for (Integer segmentID : circuitFraction.getSegments()) {
                segmentImages.add(new PathImage(segmentID, currentSegments.get(segmentID)));
            }
            CircuitImage circuitImage = (CircuitImage) getSimulationImage(String.valueOf(circuitFraction.getCircuitID()));
            transportNeedSegmentsOrganizedByCircuit.add(new CircuitFractionImage(circuitImage, segmentImages));
            segmentsID.addAll(circuitFraction.getSegments());
        }
        TransportNeedImage transportNeedImage = new TransportNeedImage(transportNeed, transportNeedSegmentsOrganizedByCircuit);
        for (Integer segmentID : segmentsID) {
            SegmentImage segmentImage = currentSegments.get(segmentID);
            nodeOrigin = addOriginIfAbsent(nodeOrigin, currentSegments, segmentID);
            if (segmentID == segmentImage.getSecondWayId()) {
                segmentImage.addReverseTransit(transportNeed.getIdentifier());
            } else {
                segmentImage.addTransit(transportNeed.getIdentifier());
            }
            transportNeedSegments.add(new PathImage(segmentID, segmentImage));
            last = segmentID;
        }
        SegmentImage firstSegment = currentSegments.get(last);
        if (firstSegment.getIdentifier() == last) {
            nodeEnd = firstSegment.getEnd();
        } else {
            nodeEnd = firstSegment.getOrigin();
        }
        transportNeedImage.setOrigin(nodeOrigin);
        transportNeedImage.setEnd(nodeEnd);
        transportNeedImage.setSegments(transportNeedSegments);
        setTransitColor(transportNeedImage);
        addChild(transportNeedImage);
    }

    private void setTransitColor(TransitImage transitImage) {
        int transitID = transitImage.getIdentifier();
        if (!transitColorPalette.containsKey(transitID)) {
            Color randomColor = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
            transitColorPalette.put(transitID, randomColor);
        }
        transitImage.setTransitColor(transitColorPalette.get(transitID));
    }

    private NodeImage addOriginIfAbsent(NodeImage nodeOrigin, HashMap<Integer, SegmentImage> currentSegments, Integer segmentID) {
        if (nodeOrigin == null) {
            SegmentImage firstSegment = currentSegments.get(segmentID);
            if (firstSegment.getIdentifier() == segmentID) {
                nodeOrigin = firstSegment.getOrigin();
            } else {
                nodeOrigin = firstSegment.getEnd();
            }
        }
        return nodeOrigin;
    }

    private void paintSimulationImages(Graphics2D graphics2D) {

        setRenderProperties(elementShape);

        int size = getContainedElement().size();
        ListIterator<SimulationImage> iter = new ArrayList<>(getContainedElement().values()).listIterator(size);
        while (iter.hasPrevious()) {
            SimulationImage image = iter.previous();
            image.paint(graphics2D);
        }
    }

    @Override
    public void setRenderProperties(Viewport viewport) {

        if (stateChanged || motion || changedElement) {
            setNodeProperties();
            stateChanged = false;
            changedElement = false;
        }
    }

    private void setNodeProperties() {
        for (SimulationImage image : getContainedElement().values()) {
            if (isANode(image)) {
                NodeImage nodeImage = (NodeImage) image;
                Ellipse2D ellipse = new Ellipse2D.Double(0, 0, 1, 1);
                AffineTransform transformToWorkPlane = new AffineTransform();

                transformToWorkPlane.setTransform(camera.transformCameraBoundariesToWorkPlane(elementShape));
                transformToWorkPlane.translate(nodeImage.getCoordinates().getXY().x, nodeImage.getCoordinates().getXY().y);
                transformToWorkPlane.scale(PREFERRED_SIMULATION_COMPONENT_SCALE, PREFERRED_SIMULATION_COMPONENT_SCALE);

                nodeImage.setRenderProperties(new Viewport(transformToWorkPlane.createTransformedShape(ellipse)));

            }
        }
    }

    void zoomIn() {
        stateChanged = true;
        camera.zoomIn();
    }

    void zoomOut() {
        stateChanged = true;
        camera.zoomOut();
    }

    double getZoomRatio() {
        return camera.getZoomRatio();
    }

    @Override
    public void removeAllChildren() {
        for (SimulationImage childName : getContainedElement().values()) {
            removeChild(childName);
        }
    }

    /**
     *
     * @param nodeImage
     */
    public void removeChild(NodeImage nodeImage) {
        super.removeChild(nodeImage);
        ArrayList<SegmentImage> segmentImages = nodeImage.getLinkedSegments();
        for (SegmentImage segmentImage : segmentImages) {
            removeChild(segmentImage);
        }
    }

    public void removeChild(SegmentImage segmentImage) {
        super.removeChild(segmentImage);
        segmentImage.removeLinkToNodes();
    }

    public void moveCameraTo(Coordinates coordinatesFromXY) {
        stateChanged = true;
        camera.moveToCoordinates(coordinatesFromXY);
    }

    private Coordinates lastCoordinates;
    private ArrayList<NodeImage> nodeToMove;
    private boolean motion;

    public Viewport getMousePositionOnMap(Viewport framedMousePosition) {
        AffineTransform mousePositionTransform = camera.transformWorkPlaneToCameraBoundaries(elementShape);
        Viewport position = new Viewport(mousePositionTransform.createTransformedShape(framedMousePosition.getBounds()).getBounds2D());
        return position;
    }

    public void startNodeMotion(ArrayList<NodeImage> nodes, Coordinates coordinates) {
        lastCoordinates = coordinates;
        nodeToMove = nodes;
    }

    public void moveNodeBy(Coordinates newCoordinates) {
        Coordinates delta = lastCoordinates.getDelta(newCoordinates);
        lastCoordinates = newCoordinates;
        if (motion) {
            for (int i = nodeToMove.size() - 1; i >= 0; i--) {
                nodeToMove.get(i).moveOf(delta);
            }
        }
        motion = true;
    }

    public void stopNodeMotion() {
        if (motion) {
            stateChanged = true;
            nodeToMove.clear();
            motion = false;
        }
    }

    private void setModified(SimulationImage image) {
        image.setModified();
    }

    void stopCreation() {
        for (NodeImage element : nodesForCreation) {
            if (element != null) {
                element.unselect();
            }
        }
        if (!nodesForCreation.isEmpty()) {
            unselectSegmentsForCreation();
        }
        nodesForCreation.clear();
    }

    void highlightCreationNodes(Integer nodeID) {
        NodeImage node = (NodeImage) getSimulationImage(String.valueOf(nodeID));
        nodesForCreation.add(node);
        node.selectForCreation();
    }

    void highlightCreationSegments() {
        unselectSegmentsForCreation();
        NodeImage origin = nodesForCreation.get(0);
        NodeImage end;
        if (origin != nodesForCreation.get(1)) {
            for (int i = 1; i < nodesForCreation.size(); ++i) {
                end = nodesForCreation.get(i);
                SegmentImage segment = getSegmentImageFromNodeImages(origin, end);
                if (segment != null && !segmentsForCreation.contains(segment)) {
                    segmentsForCreation.add(segment);
                    segment.selectForCreation();
                }
                origin = nodesForCreation.get(i);
            }
        }
    }

    void lockSegmentAndNodeChoices(ArrayList<Integer> segments) {
        for (Iterator<Integer> it = segments.iterator(); it.hasNext();) {
            Integer segment = it.next();
            SegmentImage segmentImage = (SegmentImage) getSimulationImage(String.valueOf(segment));
            if (segmentImage != null && !lockedForCreation.contains(segmentImage)) {
                lockedForCreation.add(segmentImage);
                segmentImage.lockForCreation();
            }
        }
    }

    void unlockImageChoices() {
        for (SegmentImage segmentImage : lockedForCreation) {
            if (segmentImage != null) {
                segmentImage.unlockAndUnselect();
            }
        }
    }

    private void unselectSegmentsForCreation() {
        if (!segmentsForCreation.isEmpty()) {
            for (Iterator<SegmentImage> it = segmentsForCreation.iterator(); it.hasNext();) {
                SegmentImage element = it.next();
                if (element != null) {
                    element.unselect();
                }
            }
            segmentsForCreation = new ArrayList<>();
        }
    }

    public NodeImage getNodeAt(Viewport mousePosition) {
        Displayable highest = findHighestClick(mousePosition);
        if (isANode((SimulationImage) highest)) {
            return (NodeImage) highest;
        }

        return null;
    }

    SegmentImage getSegmentAt(Viewport mousePosition) {
        Displayable highest = findHighestClick(mousePosition);
        String className = highest.getClass().getSimpleName();

        if (className.equals(SegmentImage.class
                .getSimpleName())) {
            return (SegmentImage) highest;
        }
        return null;
    }

    StationImage getStationAt(Viewport mousePosition) {
        Displayable highest = findHighestClick(mousePosition);
        String className = highest.getClass().getSimpleName();

        if (className.equals(StationImage.class
                .getSimpleName())) {
            return (StationImage) highest;
        }
        return null;
    }

    IntersectionImage getIntersectionAt(Viewport mousePosition) {
        Displayable highest = findHighestClick(mousePosition);
        String className = highest.getClass().getSimpleName();

        if (className.equals(IntersectionImage.class
                .getSimpleName())) {
            return (IntersectionImage) highest;
        }
        return null;
    }

    AnimatorImage getAnimationImage() {
        return animatorImage;
    }

    @Override
    public double getBoxHeight() {
        return elementShape.getHeight();
    }

    public SimulationImage getSimulationImage(String id) {
        return containedElement.get(id);
    }

    private boolean elementIsOfType(String id, String elementType) {
        String nameToCompare = "";
        if (elementType.equals(STATION)) {
            nameToCompare = STATION_IMAGE;
        }
        if (elementType.equals(SEGMENT)) {
            nameToCompare = SEGMENT_IMAGE;
        }
        if (elementType.equals(INTERSECTION)) {
            nameToCompare = INTERSECTION_IMAGE;
        }
        if (elementType.equals(CIRCUIT)) {
            nameToCompare = CIRCUIT_IMAGE;
        }
        if (elementType.equals(TRANSPORT_NEED)) {
            nameToCompare = TRANSPORT_NEED_IMAGE;
        }
        return getSimulationImage(id).getClass().getSimpleName().equals(nameToCompare);
    }

    private boolean notInNewArrival(String id, String elementType) throws NumberFormatException {
        return !newArrival.get(elementType).containsKey(Integer.parseInt(id));

    }

    private boolean isANode(SimulationImage image) {
        return image.getClass().getSimpleName().equals(IntersectionImage.class
                .getSimpleName()) || image.getClass().getSimpleName().equals(StationImage.class
                        .getSimpleName());
    }

    private boolean isStationImage(SimulationImage image) {
        return image.getClass().getSimpleName().equals(STATION_IMAGE);
    }

    private boolean isIntersectionImage(SimulationImage image) {
        return image.getClass().getSimpleName().equals(INTERSECTION_IMAGE);
    }

    private boolean isSegmentImage(SimulationImage image) {
        return image.getClass().getSimpleName().equals(SEGMENT_IMAGE);
    }

    private boolean isCircuitImage(SimulationImage image) {
        return image.getClass().getSimpleName().equals(CIRCUIT_IMAGE);
    }

    private boolean isTransportNeedImage(SimulationImage image) {
        return image.getClass().getSimpleName().equals(TRANSPORT_NEED_IMAGE);
    }

    void setChange() {
        stateChanged = true;
    }

    void setChangedElement() {
        changedElement = true;
    }

    public ArrayList<CircuitImage> getCircuitImages() {
        ArrayList<CircuitImage> circuits = new ArrayList<>();
        int size = getContainedElement().size();
        ListIterator<SimulationImage> iter = new ArrayList<>(getContainedElement().values()).listIterator(size);
        while (iter.hasPrevious()) {
            SimulationImage image = iter.previous();
            if (isCircuitImage(image)) {
                circuits.add((CircuitImage) image);
            }
        }
        return circuits;
    }

    public ArrayList<TransportNeedImage> getTransportNeedImages() {
        ArrayList<TransportNeedImage> transportNeed = new ArrayList<>();
        int size = getContainedElement().size();
        ListIterator<SimulationImage> iter = new ArrayList<>(getContainedElement().values()).listIterator(size);
        while (iter.hasPrevious()) {
            SimulationImage image = iter.previous();
            if (isTransportNeedImage(image)) {
                transportNeed.add((TransportNeedImage) image);
            }
        }
        return transportNeed;
    }

    public HashMap<Integer, SegmentImage> getAllSegments() {
        HashMap<Integer, SegmentImage> segments = new HashMap<>();
        int size = getContainedElement().size();
        ListIterator<SimulationImage> iter = new ArrayList<>(getContainedElement().values()).listIterator(size);
        while (iter.hasPrevious()) {
            SimulationImage image = iter.previous();
            if (isSegmentImage(image)) {
                SegmentImage segment = (SegmentImage) image;
                if (segment.isTwoWay()) {
                    segments.put(segment.getSecondWayId(), segment);
                }
                segments.put(segment.getIdentifier(), segment);
            }
        }
        return segments;
    }

    public ArrayList<StationImage> getAllStationImages() {
        ArrayList<StationImage> stations = new ArrayList<>();

        int size = getContainedElement().size();
        ListIterator<SimulationImage> iter = new ArrayList<>(getContainedElement().values()).listIterator(size);
        while (iter.hasPrevious()) {
            SimulationImage image = iter.previous();
            if (isStationImage(image)) {
                stations.add((StationImage) image);
            }
        }

        return stations;
    }

    public SegmentImage getSegmentImageFromNodeImages(NodeImage nodeImageOrigin, NodeImage nodeImageEnd) {
        int size = getContainedElement().size();
        ListIterator<SimulationImage> iter = new ArrayList<>(getContainedElement().values()).listIterator(size);
        while (iter.hasPrevious()) {
            SimulationImage image = iter.previous();
            if (isSegmentImage(image)) {
                SegmentImage segmentImage = (SegmentImage) image;
                if (matchesThisSegment(segmentImage, nodeImageOrigin, nodeImageEnd)) {
                    return segmentImage;
                }
            }
        }
        return null;
    }

    private static boolean matchesThisSegment(SegmentImage segmentImage, NodeImage nodeImageOrigin, NodeImage nodeImageEnd) {
        return (segmentImage.getOrigin().equals(nodeImageOrigin) && segmentImage.getEnd().equals(nodeImageEnd)) || (segmentImage.isTwoWay() && segmentImage.getOrigin().equals(nodeImageEnd) && segmentImage.getEnd().equals(nodeImageOrigin));
    }

    public void setDisplayMap(boolean displayMap) {
        this.displayMap = displayMap;
    }

    public boolean getDisplayMap() {
        return displayMap;
    }

    public String getReport() {
        return animatorImage.getReport();
    }

    public ArrayList<CircuitImage> getCircuitImagesFromNodeID(int nodeID) {
        ArrayList<CircuitImage> circuitImages = getCircuitImages();
        ListIterator<CircuitImage> iter = circuitImages.listIterator();
        while (iter.hasNext()) {
            boolean isPartOf = false;
            for (PathImage pathImage : iter.next().getSegments()) {
                if (pathImage.getSegmentImage().getOrigin().getIdentifier() == nodeID || pathImage.getSegmentImage().getEnd().getIdentifier() == nodeID) {
                    isPartOf = true;
                }
            }
            if (!isPartOf) {
                iter.remove();
            }
        }
        return circuitImages;
    }

    public ArrayList<Integer> getTransportNeedIDsThatUsesThatCircuit(int circuitID) {
        ArrayList<Integer> transportNeedIDs = new ArrayList<>();
        ArrayList<TransportNeedImage> transportNeedImages = getTransportNeedImages();
        ListIterator<TransportNeedImage> iter = transportNeedImages.listIterator();
        while (iter.hasNext()) {
            TransportNeedImage transportNeedImage = iter.next();
            for (CircuitFractionImage circuitFractionImage : transportNeedImage.getSegmentsOrganizedByCircuit()) {
                if (circuitFractionImage.getCircuitImage().getIdentifier() == circuitID) {
                    transportNeedIDs.add(transportNeedImage.getIdentifier());
                    break;
                }
            }
        }
        return transportNeedIDs;
    }

}
