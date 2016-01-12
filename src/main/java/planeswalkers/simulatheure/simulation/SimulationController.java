/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.simulation;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import planeswalkers.simulatheure.animation.Animator;
import planeswalkers.simulatheure.data.NetworkFactory;
import planeswalkers.simulatheure.data.SimulationElement;
import planeswalkers.simulatheure.data.networkelement.Intersection;
import planeswalkers.simulatheure.data.networkelement.NetworkElementFactory;
import planeswalkers.simulatheure.data.networkelement.Segment;
import planeswalkers.simulatheure.data.networkelement.Station;
import planeswalkers.simulatheure.data.transit.Circuit;
import planeswalkers.simulatheure.data.transit.CircuitFraction;
import planeswalkers.simulatheure.data.transit.Transit;
import planeswalkers.simulatheure.data.transit.TransitFactory;
import planeswalkers.simulatheure.data.transit.TransportNeed;
import planeswalkers.simulatheure.data.units.Coordinates;
import planeswalkers.simulatheure.ui.DataReceiver;
import planeswalkers.simulatheure.ui.components.AnimatorImage;
import planeswalkers.simulatheure.ui.components.IntersectionImage;
import planeswalkers.simulatheure.ui.components.NodeImage;
import planeswalkers.simulatheure.ui.components.SegmentImage;
import planeswalkers.simulatheure.ui.components.StationImage;
import planeswalkers.simulatheure.ui.components.Viewport;
import planeswalkers.simulatheure.ui.gui.ReportWindow;
import planeswalkers.simulatheure.ui.gui.SimulationBox;
import planeswalkers.simulatheure.ui.gui.SimulatorScreen;

/**
 *
 * @author Marc-Antoine Fortier
 */
/**
 *
 * @author Marc-Antoine Fortier
 */
public final class SimulationController {

    private final DataReceiver dataReceiver;
    private NetworkFactory networkFactory = NetworkFactory.getInstance();
    private final ArrayList<HashMap<String, HashMap<Integer, SimulationElement>>> networkHistory;
    private final ArrayList<HashMap<String, HashMap<Integer, SimulationElement>>> transitHistory;
    private int historyIndex = -1;
    protected NetworkElementFactory currentNetwork;
    protected TransitFactory currentTransit;
    private final Animator animator;
    private final SimulatorScreen screen;
    private final NetworkAnalyser analyser;

    private final SimulationBox simulationBox;

    public SimulatorScreen getScreen() {
        return screen;
    }

    private final String NETWORK_NAME = "RTC";
    private final String CIRCUIT = Circuit.class.getSimpleName();

    /**
     * SimulationController Constructor, creates a SimulationController object.
     *
     */
    public SimulationController() {
        dataReceiver = new DataReceiver();
        simulationBox = new SimulationBox(dataReceiver);
        simulationBox.setState(new NetworkEditing());
        screen = new SimulatorScreen(simulationBox);
        networkHistory = new ArrayList<>();
        transitHistory = new ArrayList<>();
        analyser = NetworkAnalyser.getInstance();

        currentNetwork = networkFactory.getNetworkElementFactory(NETWORK_NAME);
        currentTransit = networkFactory.getTransitFactory(NETWORK_NAME);

        animator = new Animator();

        updateNetworkData();

        RootListener root = new RootListener(simulationBox);
        RootWheelListener rootWheel = new RootWheelListener(simulationBox);
        RootMotionListener rootMotion = new RootMotionListener(simulationBox);
        RootKeyListener rootKey = new RootKeyListener(simulationBox);

        screen.getRenderer().addMouseListener(root);
        screen.getRenderer().addMouseWheelListener(rootWheel);
        screen.getRenderer().addMouseMotionListener(rootMotion);
        screen.getRenderer().requestFocus();
        screen.getRenderer().addKeyListener(rootKey);

        screen.startDisplay();

    }

    public void updateNetworkData() {
        HashMap<String, HashMap<Integer, SimulationElement>> originalNetwork = new HashMap<>();
        HashMap<String, HashMap<Integer, SimulationElement>> originalTransit = new HashMap<>();
        HashMap<String, HashMap<Integer, SimulationElement>> networkCopy = new HashMap<>();
        HashMap<String, HashMap<Integer, SimulationElement>> transitCopy = new HashMap<>();
        HashMap<String, HashMap<Integer, SimulationElement>> totalCopy = new HashMap<>();

        historyIndex++;
        removeRedo();

        originalNetwork.putAll(currentNetwork.getNetworkElements());
        originalTransit.putAll(currentTransit.getTransits());

        for (Entry<String, HashMap<Integer, SimulationElement>> entry : originalNetwork.entrySet()) {
            networkCopy.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
        networkHistory.add(historyIndex, networkCopy);
        for (Entry<String, HashMap<Integer, SimulationElement>> entry : originalTransit.entrySet()) {
            transitCopy.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
        transitHistory.add(historyIndex, transitCopy);

        totalCopy.putAll(networkCopy);
        totalCopy.putAll(transitCopy);
        dataReceiver.updateSimulationElements(totalCopy);
        simulationBox.updateRendering();
    }

    private void removeRedo() {
        ArrayList<HashMap<String, HashMap<Integer, SimulationElement>>> historyCopy = new ArrayList<>();
        for (HashMap<String, HashMap<Integer, SimulationElement>> keep : networkHistory) {
            if (networkHistory.indexOf(keep) < historyIndex) {
                historyCopy.add(keep);
            }
        }
        networkHistory.clear();
        networkHistory.addAll(historyCopy);
        historyCopy.clear();
        for (HashMap<String, HashMap<Integer, SimulationElement>> keep : transitHistory) {
            if (transitHistory.indexOf(keep) < historyIndex) {
                historyCopy.add(keep);
            }
        }
        transitHistory.clear();
        transitHistory.addAll(historyCopy);
    }

    /**
     * Use for undo and redo update.
     *
     * @param history The index of the required version of the network to reload
     */
    public void updateHistoryData(int history) {
        HashMap<String, HashMap<Integer, SimulationElement>> original = new HashMap<>();
        original.putAll(networkHistory.get(history));
        original.putAll(transitHistory.get(history));
        currentNetwork.setNetworkElements(networkHistory.get(history));
        currentTransit.setTransits(transitHistory.get(history));
        dataReceiver.updateSimulationElements(original);
        simulationBox.updateRendering();
    }

    public void undo() {
        if (historyIndex > 0) {
            historyIndex--;
            updateHistoryData(historyIndex);
        }
    }

    public void redo() {
        if (historyIndex + 1 < networkHistory.size()) {
            historyIndex++;
            updateHistoryData(historyIndex);
        }
    }

    private void save() {
        JFileChooser fileChooser = initFileChooser("Save");
        int result = fileChooser.showOpenDialog(screen.getScreen());
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath().replaceFirst("[.][^.]+$", "");
            networkFactory.save(NETWORK_NAME, path);
        }
    }

    private void load() {
        JFileChooser fileChooser = initFileChooser("Load");
        int result = fileChooser.showOpenDialog(screen.getScreen());
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            networkFactory.load(path);
            currentNetwork = networkFactory.getNetworkElementFactory(NETWORK_NAME);
            currentTransit = networkFactory.getTransitFactory(NETWORK_NAME);
            updateNetworkData();
        }
    }

    private JFileChooser initFileChooser(String choice) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Ser Files", "ser"));
        fileChooser.setApproveButtonText(choice);
        fileChooser.setDialogTitle(choice);
        return fileChooser;
    }

    private void startAnimation() {
        initAnimator();
        parametriseAnimator();
        animator.computeAnimation();
        dataReceiver.updateAnimations(animator.getAnimations(), animator.getReport());
    }

    private void initAnimator() {
        HashMap<String, HashMap<Integer, SimulationElement>> networkStateForAnimation = new HashMap<>();
        networkStateForAnimation.putAll(currentNetwork.getNetworkElements());
        networkStateForAnimation.putAll(currentTransit.getTransits());

        animator.initAnimator(networkStateForAnimation);
    }

    private void parametriseAnimator() {
        AnimatorImage animationImage = simulationBox.getParametrisedAnimation();

        int start = animationImage.getStartingTime();
        int end = animationImage.getEndTime();
        int replication = animationImage.getNumberOfReplication();
        animator.parametrize(start, end, replication);
    }

    private void saveNetworkElementChangement() {
        simulationBox.endEdition();
        if (simulationBox.isElementModified()) {
            if (simulationBox.isAnimationModified()) {

            } else {
                SimulationElement modifiedElement = simulationBox.getModifiedElement();
                if (isTransit(modifiedElement)) {
                    Transit modifiedTransit = (Transit) modifiedElement;
                    currentTransit.modifyTransit(modifiedTransit.getIdentifier(), modifiedTransit);
                } else {
                    currentNetwork.modifyNetworkElement(modifiedElement.getIdentifier(), modifiedElement);
                    if (simulationBox.isThereASecondSegment()) {
                        Segment secondSegment = simulationBox.getModifiedSecondWay();
                        currentNetwork.modifyNetworkElement(secondSegment.getIdentifier(), secondSegment);

                    }
                }
                updateNetworkData();
                simulationBox.refreshElementsAfterModification();
            }
        }
    }

    public void deleteNetworkElement() {
        simulationBox.saveIdAndDeleteImages();
        deleteTransportNeed();
        deleteCircuit();
        deleteSegments();
        deleteStation();
        deleteIntersection();
        updateNetworkData();
    }

    private void deleteSegments() {
        for (Integer segmentID : simulationBox.getInSelectionSegmentID()) {
            currentNetwork.removeSegment(segmentID);
        }
    }

    private void deleteIntersection() {
        for (Integer intersectionID : simulationBox.getSelectedIntersectionID()) {
            currentNetwork.removeIntersection(intersectionID);
        }
    }

    private void deleteStation() {
        for (Integer stationID : simulationBox.getSelectedStationID()) {
            currentNetwork.removeStation(stationID);
        }
    }

    private void deleteCircuit() {
        int circuitID = simulationBox.getSelectedCircuitID();
        if (circuitID != -1) {
            currentTransit.removeCircuit(circuitID);
        }
        for (Integer transitID : simulationBox.getConcernedBySelectionTransitID()) {
            if (currentTransit.getTransits().get(Circuit.class.getSimpleName()).containsKey(transitID)) {
                currentTransit.removeCircuit(transitID);
            }
            if (currentTransit.getTransits().get(TransportNeed.class.getSimpleName()).containsKey(transitID)) {
                currentTransit.removeTransportNeed(transitID);
            }
        }
    }

    private void deleteTransportNeed() {
        int transportNeedID = simulationBox.getSelectedTransportNeedID();
        if (transportNeedID != -1) {
            currentTransit.removeTransportNeed(transportNeedID);
        }
        for (Integer transitID : simulationBox.getConcernedBySelectionTransitID()) {
            if (currentTransit.getTransits().get(Circuit.class.getSimpleName()).containsKey(transitID)) {
                currentTransit.removeCircuit(transitID);
            }
            if (currentTransit.getTransits().get(TransportNeed.class.getSimpleName()).containsKey(transitID)) {
                currentTransit.removeTransportNeed(transitID);
            }
        }
    }

    public class RootKeyListener implements KeyListener {

        private final SimulationBox simulationRoot;

        public RootKeyListener(SimulationBox root) {
            this.simulationRoot = root;
        }

        /**
         * Will deal with KEYCODE only
         *
         * @param ke
         */
        @Override
        public void keyPressed(KeyEvent ke) {
            simulationBox.setSelectionAppend(ke.isControlDown());
            switch (ke.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    simulationBox.emptySelection();
                    break;
                case KeyEvent.VK_DELETE:
                    if (simulationBox.isTextEditing()) {
                        simulationBox.processKeyPressed(ke);
                    } else {
                        deleteNetworkElement();
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    saveNetworkElementChangement();
                    break;
                case KeyEvent.VK_LEFT:
                    simulationBox.leftArrow();
                    break;
                case KeyEvent.VK_RIGHT:
                    simulationBox.rightArrow();
                    break;
                default:
                    simulationBox.processKeyPressed(ke);
            }
        }

        @Override
        public void keyReleased(KeyEvent ke
        ) {
            simulationBox.setSelectionAppend(ke.isControlDown());
            if (!simulationBox.isTextEditing()) {
                if (ke.getKeyCode() == KeyEvent.VK_Z && ke.isControlDown()) {
                    undo();
                }
                if (ke.getKeyCode() == KeyEvent.VK_Y && ke.isControlDown()) {
                    redo();
                }
                if (ke.getKeyCode() == KeyEvent.VK_S && ke.isControlDown()) {
                    save();
                }
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }

    /**
     *
     */
    public class RootListener implements MouseListener {

        private final SimulationBox simulationRoot;

        /**
         *
         * @param root the SimulationBox containing all the Displayable elements
         * of the Simulation
         */
        public RootListener(SimulationBox root) {
            this.simulationRoot = root;
        }

        /**
         *
         * @param e
         */
        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                simulationBox.rightPress(new Viewport(e.getX(), e.getY()));
            }
            if (SwingUtilities.isLeftMouseButton(e)) {
                simulationBox.leftPress(new Viewport(e.getX(), e.getY()));
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            if (SwingUtilities.isRightMouseButton(e)) {
                simulationBox.rightClick(new Viewport(e.getX(), e.getY()));
            }
            if (SwingUtilities.isLeftMouseButton(e)) {
                simulationBox.leftClick(new Viewport(e.getX(), e.getY()));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            simulationBox.mouseRelease();
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    public class RootMotionListener implements MouseMotionListener {

        private final SimulationBox simulationRoot;

        /**
         *
         * @param root the SimulationBox containing all the Displayable elements
         * of the Simulation
         */
        public RootMotionListener(SimulationBox root) {
            this.simulationRoot = root;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            simulationBox.mouseMoved(new Viewport(e.getX(), e.getY()));

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                simulationBox.moveSomething(new Viewport(e.getX(), e.getY()));
            }
        }

    }

    public class RootWheelListener implements MouseWheelListener {

        private final SimulationBox simulationRoot;

        /**
         *
         * @param root the SimulationBox containing all the Displayable elements
         * of the Simulation
         */
        public RootWheelListener(SimulationBox root) {
            this.simulationRoot = root;
        }

        /**
         *
         * @param e
         */
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (simulationBox.onBackground()) {
                simulationBox.zoom(e.getPreciseWheelRotation());
            }
        }
    }

    /**
     * Simulator State
     *
     * @author Jean-BenoÃ®t
     */
    public interface SimulationState {

        void rightPress(Viewport mousePosition);

        void leftPress(Viewport mousePosition);

        void rightClick(Viewport mousePosition);

        void leftClick(Viewport mousePosition);

        void mouseMoved(Viewport mousePosition);

        void unpress();

    }

    /**
     * NetworkEditing
     *
     * @author Jean-BenoÃ®t
     */
    public class NetworkEditing implements SimulationState {

        public NetworkEditing() {
            simulationBox.dropSelection();
        }

        @Override
        public void rightPress(Viewport mousePosition) {

            if (simulationBox.onBackground()) {
                simulationBox.openContextualMenu();
                simulationBox.setState(new CreationMenu());
            }
        }

        @Override
        public void leftPress(Viewport mousePosition) {
        }

        @Override
        public void rightClick(Viewport mousePosition) {
        }

        @Override
        public void leftClick(Viewport mousePosition) {
            SimulationState newState = new NetworkEditing();
            saveNetworkElementChangement();
            if (simulationBox.onWorkplane()) {
                simulationBox.processImageSelection();
            } else if (simulationBox.onTopMenu()) {
                topMenuAction();
            } else if (simulationBox.onAnimationTools()) {
                newState = animationToolsAction();
            } else if (simulationBox.onDelete()) {
                deleteNetworkElement();
            } else if (simulationBox.onEditableText()) {
                simulationBox.loadText();
            } else if (simulationBox.onTransitGatheringBox()) {
                if (simulationBox.onAdd()) {
                    if (simulationBox.areTransportNeedAppended()) {
                        newState = new AddTransportNeed();
                    } else if (simulationBox.areCircuitAppended()) {
                        newState = new AddCircuit();
                    }
                } else if (simulationBox.onPartialDelete()) {
                    simulationBox.storeTransit();
                    deleteNetworkElement();
                } else if (simulationBox.onEditTransit()) {
                    simulationBox.openTransit();
                }
            } else if (simulationBox.onNetworkEditionBox()) {

                if (simulationBox.onConfirm()) {
                    saveNetworkElementChangement();
                    simulationBox.emptySelection();
                }

            }
            simulationBox.setState(newState);

        }

        @Override
        public void mouseMoved(Viewport mousePosition) {
        }

        @Override
        public void unpress() {
            if (simulationBox.isElementInMotion()) {
                updateNodePosition();
                simulationBox.stopElementMotion();
            }
        }

        private void updateNodePosition() {
            for (NodeImage nodeImage : simulationBox.getNodesToMove()) {
                if (nodeImage.getClass().getSimpleName().equals(IntersectionImage.class.getSimpleName())) {
                    int identifier = nodeImage.getIdentifier();
                    Intersection tempoIntersection = new Intersection(nodeImage.getDisplayName(), identifier, nodeImage.getCoordinates());
                    currentNetwork.modifyNetworkElement(identifier, tempoIntersection);
                } else if (nodeImage.getClass().getSimpleName().equals(StationImage.class.getSimpleName())) {
                    int identifier = nodeImage.getIdentifier();
                    Station tempoStation = new Station(nodeImage.getDisplayName(), identifier, nodeImage.getCoordinates());
                    currentNetwork.modifyNetworkElement(identifier, tempoStation);
                }
            }
            updateNetworkData();
        }

        private void topMenuAction() {
            if (simulationBox.onChannelUp()) {
                simulationBox.foldTopMenu();
            } else if (simulationBox.onChannelDown()) {
                simulationBox.unfoldTopMenu();
            } else if (simulationBox.onOpenFile()) {
                load();
            } else if (simulationBox.onSave()) {
                save();
            } else if (simulationBox.onUndo()) {
                undo();
            } else if (simulationBox.onRedo()) {
                redo();
            } else if (simulationBox.onEarth()) {
                if (simulationBox.getDisplayMap()) {
                    simulationBox.setDisplayMap(false);
                } else {
                    simulationBox.setDisplayMap(true);
                }
            } else if (simulationBox.onQuit()) {
                WindowEvent wev = new WindowEvent(getScreen().getScreen(), WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
            }
        }

        private SimulationState animationToolsAction() {
            SimulationState newState = new NetworkEditing();
            if (simulationBox.onChannelRight()) {
                simulationBox.foldAnimationTools();
            } else if (simulationBox.onChannelLeft()) {
                simulationBox.unfoldAnimationTools();
            } else if (simulationBox.onAnimationParameters()) {
                simulationBox.openAnimationParameters();
            } else if (simulationBox.onStartAnimation()) {
                startAnimation();
                newState = new AnimationState();
            }
            return newState;
        }

    }

    /**
     * AddSegment
     *
     * @author Marc-Antoine
     */
    public class AddSegment extends NetworkEditing {

        private int nodeCounter = 2;
        private NodeImage node1, node2;
        private SegmentImage segmentImage;

        public AddSegment() {
            simulationBox.emptySelection();
            node1 = null;
            node2 = null;
        }

        @Override
        public void leftPress(Viewport mousePosition) {
        }

        @Override
        public void leftClick(Viewport mousePosition) {
            if (simulationBox.onNode()) {
                if (node1 == null) {
                    node1 = (NodeImage) simulationBox.getClickedOnElement();
                    nodeCounter--;
                } else if (node1 != null && node2 == null) {
                    node2 = (NodeImage) simulationBox.getClickedOnElement();
                    if (node1 != node2) {
                        currentNetwork.createSegment(node1.getIdentifier(), node2.getIdentifier());
                        updateNetworkData();
                    }
                    node1 = node2;
                    node2 = null;
                }
            }
        }

        @Override
        public void mouseMoved(Viewport mousePosition) {
        }
    }

    /**
     * AddStation
     *
     * @author Jean-BenoÃ®t
     */
    public class AddStation extends NetworkEditing {

        public AddStation() {
            simulationBox.emptySelection();
        }

        @Override
        public void leftPress(Viewport mousePosition) {

            if (!simulationBox.onNode()) {
                Coordinates stationcoordinates = simulationBox.getCurrentCoordinates();
                currentNetwork.createStation(stationcoordinates);
                updateNetworkData();
            }

        }

        @Override
        public void leftClick(Viewport mousePosition) {

        }

        @Override
        public void mouseMoved(Viewport mousePosition) {

        }

    }

    /**
     * Add Intersection
     *
     * @author Jean-BenoÃ®t
     */
    public class AddIntersection extends NetworkEditing {

        public AddIntersection() {
            simulationBox.emptySelection();
        }

        @Override
        public void rightPress(Viewport mousePosition) {
            simulationBox.setState(new NetworkEditing());
        }

        @Override
        public void leftPress(Viewport mousePosition) {

            if (!simulationBox.onNode()) {
                Coordinates intersectionCoordinates = simulationBox.getCurrentCoordinates();
                currentNetwork.createIntersection(intersectionCoordinates);
                updateNetworkData();
            }

        }

        @Override
        public void leftClick(Viewport mousePosition) {
        }

        @Override
        public void rightClick(Viewport mousePosition) {

        }

        @Override
        public void mouseMoved(Viewport mousePosition) {
        }

    }

    public class AddTransportNeed extends NetworkEditing {

        private final String START = "Start";
        private final String END = "End";

        private LinkedHashMap<Integer, HashMap<String, Integer>> handPickedSegments;
        private ArrayList< CircuitFraction> circuitsFractions;
        private HashMap<String, Integer> handPickedNodes;
        private int currentCircuitID = -1;
        private int lastPickedNode;
        private Circuit currentCircuit;
        private ArrayList<Integer> possibleSegmentChoices;
        private ArrayList<Integer> dijkstraPath;
        private boolean pickingCircuit = false;

        public AddTransportNeed() {
            simulationBox.setUpCreation();
            possibleSegmentChoices = new ArrayList<>();
            analyser.refreshNetwork(currentNetwork.getNetworkElements().get(Segment.class.getSimpleName()));
            handPickedSegments = new LinkedHashMap<>();
            circuitsFractions = new ArrayList<>();
            initPickedNodes(-1);
            System.out.println(analyser.toString());
        }

        private void initPickedNodes(int startOfNewCircuitFraction) {
            handPickedNodes = new HashMap<>();
            handPickedNodes.put(START, startOfNewCircuitFraction);
            handPickedNodes.put(END, -1);
        }

        @Override
        public void leftClick(Viewport mousePosition) {
            if (simulationBox.onNode()) {
                if (handPickedNodes.get(START) == -1) {
                    pickFirstNode();
                    pickingCircuit = true;
                } else if (!pickingCircuit) {
                    lastPickedNode = simulationBox.getSelectedElementID();
                    if (!analyser.isNodeLocked(lastPickedNode)) {
                        if (handPickedNodes.get(END) == -1) {
                            pickLastNode();
                        } else {
                            pickMiddleNode();
                        }
                        formPathFromNodes();
                        promptCircuitChoser();
                    }
                }
            } else {
                actionForButtons();
            }
        }

        private void promptCircuitChoser() {
            simulationBox.appendCircuitForSelection(lastPickedNode);
        }

        private void pickFirstNode() {
            ArrayList<Integer> firstNodes = new ArrayList<>();
            lastPickedNode = simulationBox.getSelectedElementID();
            handPickedNodes.put(START, lastPickedNode);
            firstNodes.add(lastPickedNode);
            simulationBox.highlightCreation(firstNodes);
            simulationBox.appendCircuitForSelection(lastPickedNode);
        }

        protected void pickLastNode() {
            if (!analyser.isNodeLocked(lastPickedNode)) {
                handPickedNodes.put(END, lastPickedNode);
            }
        }

        protected void formPathFromNodes() {
            dijkstraPath = new ArrayList<>();
            int startNode, endNode, currentNode;
            int currentPosition = 3;
            int size = handPickedNodes.size();
            startNode = handPickedNodes.get(START);
            endNode = handPickedNodes.get(END);
            if (endNode != -1) {
                analyser.dijkstra(startNode);
                System.out.println("Start Node : " + startNode);
                while (currentPosition <= size) {
                    currentNode = handPickedNodes.get(String.valueOf(currentPosition));
                    System.out.println("Current Node : " + currentNode + "( " + currentPosition + ")");
                    dijkstraPath.addAll(analyser.getShortestPathInNodes(startNode, currentNode));
                    dijkstraPath.remove(dijkstraPath.size() - 1);
                    startNode = currentNode;
                    analyser.dijkstra(startNode);
                    currentPosition++;
                }
                System.out.println("End Node : " + endNode);
                dijkstraPath.addAll(analyser.getShortestPathInNodes(startNode, endNode));
//                showSelectedPath();
                simulationBox.stopCreation();
                simulationBox.highlightCreation(dijkstraPath);
            }
        }

        protected void removeLastChoice() {
            if (handPickedNodes.size() > 2) {
                handPickedNodes.remove(String.valueOf(handPickedNodes.size()));
            }
        }

        protected void showSelectedPath() {
            System.out.println("Path : \n");
            for (Integer node : dijkstraPath) {
                System.out.print(node + " - ");
            }
            System.out.println();
        }

        protected void pickMiddleNode() {
            lastPickedNode = simulationBox.getSelectedElementID();
            if (!analyser.isNodeLocked(lastPickedNode) || lastPickedNode != handPickedNodes.get(END) || lastPickedNode != handPickedNodes.get(START)) {
                int currentPosition = handPickedNodes.size() + 1;
                handPickedNodes.put(String.valueOf(currentPosition), lastPickedNode);
            }
        }

        private void actionForButtons() {
            if (simulationBox.onCircuitChooser()) {
                if ((simulationBox.onCircuitButton())) {
                    pickingCircuit = false;
                    if (handPickedNodes.get(END) != -1) {
                        packFraction();
                    }
                    currentCircuitID = simulationBox.chooseCircuitFromID();
                    currentCircuit = (Circuit) currentTransit.getTransits().get(CIRCUIT).get(currentCircuitID);
                    analyser.lockOnCircuit(currentCircuit);
                    analyser.refreshNetwork(currentNetwork.getNetworkElements().get(Segment.class.getSimpleName()));
                }
            } else if (simulationBox.onConfirm()) {
                ArrayList<Integer> selectedSegments = analyser.getTransitSegmentID(dijkstraPath);
                System.out.println(currentCircuitID);
                CircuitFraction selectedFraction = new CircuitFraction(currentCircuitID, selectedSegments);
                handPickedSegments.put(currentCircuitID, handPickedNodes);
                circuitsFractions.add(selectedFraction);
                currentTransit.createTransportNeed(circuitsFractions);
                updateNetworkData();
                exitState();
            } else if (simulationBox.onUndo()) {
                removeLastChoice();
                formPathFromNodes();
            } else if (simulationBox.onCancel()) {
                exitState();
            }
        }

        private void exitState() {
            simulationBox.stopCreation();
            simulationBox.removePathEditionChoices();
            simulationBox.setState(new NetworkEditing());
            analyser.removeLock();
            simulationBox.unlockCreationChoices();
        }

        private void packFraction() {
            try {
                ArrayList<Integer> selectedSegments = analyser.getTransitSegmentID(dijkstraPath);
                CircuitFraction selectedFraction = new CircuitFraction(currentCircuitID, selectedSegments);
                handPickedSegments.put(currentCircuitID, handPickedNodes);
                circuitsFractions.add(selectedFraction);
                simulationBox.lockTransitSeleciton(circuitsFractions);
            } catch (IndexOutOfBoundsException | NullPointerException npe) {
                if (dijkstraPath.size() > 2) {
                    removeLastChoice();
                    formPathFromNodes();
                    packFraction();
                }
            }
            initPickedNodes(handPickedNodes.get(END));
        }

        @Override
        public void mouseMoved(Viewport mousePosition) {
        }
    }

    public class AddCircuit extends NetworkEditing {

        private final String START = "Start";
        private final String END = "End";

        private HashMap<String, Integer> handPickedNodes;
        private ArrayList<Integer> selectedSegments;
        private ArrayList<Integer> dijkstraPath;

        public AddCircuit() {
            simulationBox.setUpCreation();
            analyser.refreshNetwork(currentNetwork.getNetworkElements().get(Segment.class.getSimpleName()));
            handPickedNodes = new HashMap<>();
            handPickedNodes.put(START, -1);
            handPickedNodes.put(END, -1);
            System.out.println(analyser.toString());
        }

        @Override
        public void rightPress(Viewport mousePosition) {
            simulationBox.setState(new NetworkEditing());
            simulationBox.stopCreation();
        }

        @Override
        public void leftPress(Viewport mousePosition) {
        }

        @Override
        public void leftClick(Viewport mousePosition) {
            if (simulationBox.onNode()) {
                if (handPickedNodes.get(START) == -1) {
                    pickNode(START);
                } else if (handPickedNodes.get(END) == -1) {
                    pickNode(END);
                } else {
                    pickMiddleNode();
                }
                formPathFromNodes();
            } else if (simulationBox.onPathEditionChoices()) {
                actionForButtons();

            }

        }

        private void actionForButtons() {
            if (simulationBox.onConfirm()) {
                selectedSegments = analyser.getTransitSegmentID(dijkstraPath);
                currentTransit.createCircuit(selectedSegments);
                updateNetworkData();
                simulationBox.stopCreation();
                simulationBox.removePathEditionChoices();
                simulationBox.setState(new NetworkEditing());
            } else if (simulationBox.onUndo()) {
                removeLastChoice();
                formPathFromNodes();
            } else if (simulationBox.onCancel()) {
                simulationBox.stopCreation();
                simulationBox.removePathEditionChoices();
                simulationBox.setState(new NetworkEditing());

            }
        }

        protected void pickNode(String position) {
            handPickedNodes.put(position, simulationBox.getSelectedElementID());
        }

        protected void pickMiddleNode() {
            int newNode = simulationBox.getSelectedElementID();
            if (newNode != handPickedNodes.get(END) || newNode != handPickedNodes.get(START)) {
                int currentPosition = handPickedNodes.size() + 1;
                handPickedNodes.put(String.valueOf(currentPosition), newNode);
            }
        }

        protected void removeLastChoice() {
            if (handPickedNodes.size() > 2) {
                handPickedNodes.remove(String.valueOf(handPickedNodes.size()));
            }
        }

        protected void formPathFromNodes() {
            dijkstraPath = new ArrayList<>();
            int startNode, endNode, currentNode;
            int currentPosition = 3;
            int size = handPickedNodes.size();
            startNode = handPickedNodes.get(START);
            endNode = handPickedNodes.get(END);
            if (endNode != -1) {
                analyser.dijkstra(startNode);
                System.out.println("Start Node : " + startNode);
                while (currentPosition <= size) {
                    currentNode = handPickedNodes.get(String.valueOf(currentPosition));
                    System.out.println("Current Node : " + currentNode + "( " + currentPosition + ")");
                    dijkstraPath.addAll(analyser.getShortestPathInNodes(startNode, currentNode));
                    dijkstraPath.remove(dijkstraPath.size() - 1);
                    startNode = currentNode;
                    analyser.dijkstra(startNode);
                    currentPosition++;
                }
                System.out.println("End Node : " + endNode);
                dijkstraPath.addAll(analyser.getShortestPathInNodes(startNode, endNode));
                showSelectedPath();
                simulationBox.stopCreation();
                simulationBox.highlightCreation(dijkstraPath);
            }
        }

        protected void showSelectedPath() {
            System.out.println("Path : \n");
            for (Integer node : dijkstraPath) {
                System.out.print(node + " - ");
            }
            System.out.println();
        }

        @Override
        public void mouseMoved(Viewport mousePosition) {
        }
    }

    /**
     *
     * @author Jean-BenoÃ®t
     */
    public class CreationMenu implements SimulationState {

        public CreationMenu() {
        }

        @Override
        public void rightPress(Viewport mousePosition) {
        }

        @Override
        public void leftPress(Viewport mousePosition) {
            if (simulationBox.onCreationMenu()) {
                simulationBox.removeContextualCreationMenu();
                SimulationState newState = new NetworkEditing();
                if (simulationBox.onAddStation()) {
                    newState = new AddStation();
                } else if (simulationBox.onAddIntersection()) {
                    newState = new AddIntersection();
                } else if (simulationBox.onAddSegment()) {
                    newState = new AddSegment();
                } else if ((simulationBox.onCircuitButton())) {
                    simulationBox.appendCircuits();
                } else if (simulationBox.onTransportNeedButton()) {
                    simulationBox.appendTransportNeed();
                }
                simulationBox.setState(newState);
            }
        }

        @Override
        public void rightClick(Viewport mousePosition) {

        }

        @Override
        public void leftClick(Viewport mousePosition) {

        }

        @Override
        public void mouseMoved(Viewport mousePosition) {

        }

        @Override
        public void unpress() {
        }

    }

    public class AnimationState extends NetworkEditing {

        public AnimationState() {
            simulationBox.startAnimation();
        }

        @Override
        public void rightPress(Viewport mousePosition) {
        }

        @Override
        public void leftClick(Viewport mousePosition) {
            if (simulationBox.onAnimationControlBar()) {
                if (simulationBox.onPlay()) {
                    simulationBox.playAnimation();
                } else if (simulationBox.onPause()) {
                    simulationBox.pauseAnimation();
                } else if (simulationBox.onStop()) {
                    simulationBox.stopAnimation();
                    simulationBox.setState(new NetworkEditing());
                } else if ((simulationBox.onRestart())) {
                    simulationBox.restartReplication();
                } else if (simulationBox.onForward()) {
                    simulationBox.forwardAnHour();
                } else if (simulationBox.onRewind()) {
                    simulationBox.backAnHour();
                } else if (simulationBox.onNextReplication()) {
                    simulationBox.nextReplication();
                } else if (simulationBox.onPreviousReplication()) {
                    simulationBox.previousReplication();
                } else if (simulationBox.onProgressBar()) {
                    simulationBox.setClickedTime();
                } else if (simulationBox.onRabbit()) {
                    simulationBox.faster();
                } else if (simulationBox.onTurtle()) {
                    simulationBox.slower();
                }
            }
        }

        @Override
        public void mouseMoved(Viewport mousePosition) {
            String reportContent = simulationBox.getAnimationReport();
            if (!reportContent.equals(" ")) {
                simulationBox.setState(new NetworkEditing());
                ReportWindow reportWindow = new ReportWindow(reportContent);
            }
        }

    }

    private
            boolean isTransit(SimulationElement modifiedElement) {
        return modifiedElement.getClass().getSimpleName().equals(TransportNeed.class
                .getSimpleName()) || modifiedElement.getClass().getSimpleName().equals(Circuit.class
                        .getSimpleName());
    }
}
