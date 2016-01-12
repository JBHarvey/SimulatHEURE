/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and unfold the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ListIterator;
import planeswalkers.simulatheure.data.SimulationElement;
import planeswalkers.simulatheure.data.networkelement.Segment;
import planeswalkers.simulatheure.data.transit.CircuitFraction;
import planeswalkers.simulatheure.data.units.Coordinates;
import planeswalkers.simulatheure.data.units.TriangularDistribution;
import planeswalkers.simulatheure.simulation.SimulationController.SimulationState;
import planeswalkers.simulatheure.ui.DataReceiver;
import planeswalkers.simulatheure.ui.components.AnimatorImage;
import planeswalkers.simulatheure.ui.components.Box;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.AnimationParameters;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Forward;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.NextReplication;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Pause;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Play;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.PreviousReplication;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Rabbit;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Restart;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Rewind;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.StartAnimation;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Stop;
import planeswalkers.simulatheure.ui.components.Buttons.Animation.Turtle;
import planeswalkers.simulatheure.ui.components.Buttons.CancelButton;
import planeswalkers.simulatheure.ui.components.Buttons.ConfirmButton;
import planeswalkers.simulatheure.ui.components.Buttons.DeleteButton;
import planeswalkers.simulatheure.ui.components.Buttons.PartialDelete;
import planeswalkers.simulatheure.ui.components.Buttons.SquareCancelButton;
import planeswalkers.simulatheure.ui.components.Buttons.SquareConfirmButton;
import planeswalkers.simulatheure.ui.components.Buttons.SquareDeleteButton;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.ChannelDown;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.ChannelLeft;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.ChannelRight;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.ChannelUp;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.CircularUndo;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.Earth;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.OpenFile;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.Quit;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.Redo;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.Save;
import planeswalkers.simulatheure.ui.components.Buttons.menuOptions.Undo;
import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.Add;
import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.AddIntersectionButton;
import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.AddSegmentButton;
import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.AddStationButton;
import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.CircuitButton;
import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.EditTransit;
import planeswalkers.simulatheure.ui.components.Buttons.networkEdition.TransportNeedButton;
import planeswalkers.simulatheure.ui.components.CircuitImage;
import planeswalkers.simulatheure.ui.components.CornerDeleteMenu;
import planeswalkers.simulatheure.ui.components.Displayable;
import planeswalkers.simulatheure.ui.components.IntersectionImage;
import planeswalkers.simulatheure.ui.components.NetworkEditionBox;
import planeswalkers.simulatheure.ui.components.NodeImage;
import planeswalkers.simulatheure.ui.components.SegmentImage;
import planeswalkers.simulatheure.ui.components.SimulationImage;
import planeswalkers.simulatheure.ui.components.StationImage;
import planeswalkers.simulatheure.ui.components.Text;
import planeswalkers.simulatheure.ui.components.TransitImage;
import planeswalkers.simulatheure.ui.components.TransitSummaryBox;
import planeswalkers.simulatheure.ui.components.TransportNeedImage;
import planeswalkers.simulatheure.ui.components.Viewport;

/**
 *
 * @author Jean-Benoit
 */
public class SimulationBox extends Box<String, Box> {

    private BufferedImage currentCursor = null;
    private Coordinates currentCoordinates;
    private AnimatorImage currentAnimation;
    private SimulationState state;
    private Displayable elementUnderMouse;
    private Displayable focussedElement;
    private TransitImage selectedTransit;
    private CircuitImage selectedCircuit;
    private Text textInEdition;

    private final int width = SimulatorScreen.WIDTH;
    private final int height = SimulatorScreen.HEIGHT;

    private double cursorX = 0;
    private double cursorY = 0;
    private Viewport cursorPosition;

    private final String ANIMATION_IMAGE = AnimatorImage.class.getSimpleName();
    private final String SEGMENT_IMAGE = SegmentImage.class.getSimpleName();
    private final String STATION_IMAGE = StationImage.class.getSimpleName();
    private final String INTERSECTION_IMAGE = IntersectionImage.class.getSimpleName();
    private final String TRANSIT_SUMMARY_BOX = TransitSummaryBox.class.getSimpleName();
    private final String CIRCUIT_IMAGE = CircuitImage.class.getSimpleName();
    private final String TRANSPORT_NEED_IMAGE = TransportNeedImage.class.getSimpleName();
    private final String CIRCUIT_CHOICE = CircuitChooser.CircuitChoice.class.getSimpleName();

    private boolean cameraInMotion = false;
    private boolean elementInMotion = false;
    private boolean textEditing = false;

    private final ArrayList<SimulationImage> selectedElements;

    private ArrayList<NodeImage> nodesToMove;
    private ArrayList<Integer> inSelectionSegmentsIDs;
    private ArrayList<Integer> selectedStationIDs;
    private ArrayList<Integer> selectedIntersectionIDs;
    private ArrayList<Integer> concernedBySelectionTransitsIDs;
    private int selectedCircuitID;
    private int selectedTransportNeedID;

    private SimulationElement modifiedElement;
    private Segment modifiedSecondWay;
    private boolean elementModified = false;
    private boolean animationModified = false;

    private final WorkPlane workPlane;
    private final StatusBar statusBar;
    private final TopMenu topMenu;
    private final AnimationTools animationTools;
    private final AnimationControlBar animationControlBar;

    private boolean appending = false;

    private final NetworkEditionBox networkEditionBox;
    private final TransitGatheringBox transitGatheringBox;
    private final CircuitChooser circuitChooser;
    private final Viewport editionBoxProperties = new Viewport(200, 30, 350, 20);

    private final ContextualCreationMenu contextualCreationMenu;
    private final CornerDeleteMenu cornerDeleteMenu;
    private final PathEditionChoices pathEditionChoices;

    public SimulationBox(DataReceiver dataReceiver) {
        super(SimulationBox.class.getSimpleName(), 0, 0);

        elementShape = new Viewport(0, 0, width, height);
        selectedElements = new ArrayList<>();
        nodesToMove = new ArrayList<>();

        workPlane = new WorkPlane(dataReceiver);
        addChild(workPlane);

        statusBar = new StatusBar(elementShape);
        addChild(statusBar);

        topMenu = new TopMenu(0, 0);
        addChild(topMenu);

        animationTools = new AnimationTools(width, 50);
        addChild(animationTools);
        animationControlBar = new AnimationControlBar(0, 1100);

        networkEditionBox = new NetworkEditionBox(NetworkEditionBox.class.getSimpleName(), 150, 40);
        transitGatheringBox = new TransitGatheringBox(150, 40);
        circuitChooser = new CircuitChooser(150, 40);

        contextualCreationMenu = new ContextualCreationMenu();

        pathEditionChoices = new PathEditionChoices();

        cornerDeleteMenu = new CornerDeleteMenu();
    }

    void removeIfPresent(Box toRemove) {
        if (getContainedElement().containsValue(toRemove)) {
            toRemove.setRenderProperties(new Viewport(-width, -height));
            removeChild(toRemove);
        }
    }

    public void removeContextualCreationMenu() {
        contextualCreationMenu.setRenderProperties(new Viewport(-width, -height));
        removeChild(contextualCreationMenu);
    }

    public void openContextualMenu() {
        contextualCreationMenu.setRenderProperties(cursorPosition);
        addChild(contextualCreationMenu);
    }

    public void closeEditionBox() {
        networkEditionBox.close();
    }

    public void closeTransitBox() {
        transitGatheringBox.close();
    }

    public void closeCircuitChooser() {
        circuitChooser.close();
    }

    public int chooseCircuitFromID() {
        int circuitID = storeCircuit();
        closeCircuitChooser();
        removeGatheringBoxes();
        return circuitID;
    }

    public void openTransit() {
        storeTransit();
        closeTransitBox();
        closeEditionBox();
        removeGatheringBoxes();
        openNetworkEditionBoxes();
    }

    public void openAnimationParameters() {
        emptySelection();
        closeEditionBox();
        openNetworkEditionBoxes();
    }

    public void openNetworkEditionBoxes() {
        if (onSegment()) {
            networkEditionBox.openSegment((SegmentImage) elementUnderMouse);
        } else if (onStation()) {
            networkEditionBox.openStation((StationImage) elementUnderMouse);
        } else if (onIntersection()) {
            networkEditionBox.openIntersection((IntersectionImage) elementUnderMouse);
        } else if (onAnimationParameters()) {
            networkEditionBox.openAnimationParameters((AnimatorImage) currentAnimation);
        } else if (selectedTransit != null && selectedCircuitID != -1) {
            networkEditionBox.openCircuit((CircuitImage) selectedTransit);
        } else if (selectedTransit != null && selectedTransportNeedID != -1) {
            networkEditionBox.openTransportNeed((TransportNeedImage) selectedTransit);
        }
        networkEditionBox.setRenderProperties(editionBoxProperties);
        addChild(networkEditionBox);
        cornerDeleteMenu.setRenderProperties(new Viewport(width - 80, height - 180));
        addChild(cornerDeleteMenu);
    }

    public void appendCircuits() {
        emptySelection();
        transitGatheringBox.startAppendCircuit();
        ArrayList<CircuitImage> circuits = workPlane.getCircuitImages();
        ListIterator<CircuitImage> iter = new ArrayList<>(circuits).listIterator(circuits.size());
        while (iter.hasPrevious()) {
            transitGatheringBox.append((CircuitImage) iter.previous());
        }
        transitGatheringBox.setRenderProperties(editionBoxProperties);
        addChild(transitGatheringBox);
    }

    public void appendCircuitForSelection(int nodeID) {
        circuitChooser.startAppendCircuit();
        ArrayList<CircuitImage> circuits = workPlane.getCircuitImagesFromNodeID(nodeID);
        ListIterator<CircuitImage> iter = new ArrayList<>(circuits).listIterator(circuits.size());
        while (iter.hasPrevious()) {
            circuitChooser.append((CircuitImage) iter.previous());
        }
        circuitChooser.setRenderProperties(editionBoxProperties);
        addChild(circuitChooser);
    }

    public void appendTransportNeed() {
        emptySelection();
        transitGatheringBox.startAppendTransportNeed();
        ArrayList<TransportNeedImage> transportNeedImage = workPlane.getTransportNeedImages();
        ListIterator<TransportNeedImage> iter = new ArrayList<>(transportNeedImage).listIterator(transportNeedImage.size());
        while (iter.hasPrevious()) {
            transitGatheringBox.append((TransportNeedImage) iter.previous());
        }
        transitGatheringBox.setRenderProperties(editionBoxProperties);
        addChild(transitGatheringBox);
    }

    public void removeGatheringBoxes() {
        removeIfPresent(transitGatheringBox);
        removeIfPresent(circuitChooser);
    }

    public void removeNetworkEditionBoxes() {
        removeIfPresent(networkEditionBox);
    }

    public void removeCornerDeleteButton() {
        cornerDeleteMenu.setRenderProperties(new Viewport(-width, -height));
        removeChild(cornerDeleteMenu);
    }

    public void removePathEditionChoices() {
        pathEditionChoices.setRenderProperties(new Viewport(-width, -height));
        removeChild(pathEditionChoices);
    }

    public void unfoldTopMenu() {
        topMenu.unfold();
    }

    public void foldTopMenu() {
        topMenu.fold();
    }

    public void foldAnimationTools() {
        animationTools.fold();
    }

    public void setUpCreation() {
        emptySelection();
        pathEditionChoices.setRenderProperties(new Viewport(width - 230, height - 280));
        addChild(pathEditionChoices);
    }

    public void unfoldAnimationTools() {
        if (currentAnimation == null) {
            currentAnimation = workPlane.getAnimationImage();
        }
        animationTools.unfold();
    }

    public void lockTransitSeleciton(ArrayList<CircuitFraction> circuitsFractions) {
        for (CircuitFraction circuitsFraction : circuitsFractions) {
            workPlane.lockSegmentAndNodeChoices(circuitsFraction.getSegments());
        }
    }

    public void unlockCreationChoices() {
        stopCreation();
        workPlane.unlockImageChoices();
    }

    public void highlightCreation(ArrayList<Integer> nodePath) {
        workPlane.stopCreation();
        for (Integer nodes : nodePath) {
            workPlane.highlightCreationNodes(nodes);
        }
        if (nodePath.size() > 1) {
            workPlane.highlightCreationSegments();
        }
    }

    public void stopCreation() {
        workPlane.stopCreation();
        emptySelection();
    }

    /**
     * Actions of states
     *
     * @param <S>
     * @param state
     */
    public <S extends SimulationState> void setState(S state) {
        this.state = state;
    }

    public void dropCursor() {

    }

    public Displayable updateFocussedElement(Viewport mousePosition) {
        elementUnderMouse = findHighestClick(mousePosition);
        changeFocusIfNeeded();
        cursorPosition = mousePosition;
        cursorX = mousePosition.getX();
        cursorY = mousePosition.getY();
        return elementUnderMouse;
    }

    public void updateCoordinates(Viewport mouseCoordinates) {
        Viewport mousePoint = workPlane.getMousePositionOnMap(mouseCoordinates);
        currentCoordinates = Coordinates.getCoordinatesFromXY(mousePoint.toPoint());
        statusBar.updateCoordinates(currentCoordinates);
    }

    public void changeFocusIfNeeded() {
        if (elementUnderMouse != focussedElement) {
            elementUnderMouse.focus();
            if (focussedElement != null) {
                focussedElement.unfocus();
            }
            focussedElement = elementUnderMouse;
        }
    }

    public void dropSelection() {
        focussedElement = null;
    }

    public void mouseMoved(Viewport mousePosition) {
        updateFocussedElement(mousePosition);
        updateCoordinates(mousePosition);
        cursorX = mousePosition.getX();
        cursorY = mousePosition.getY();
        state.mouseMoved(mousePosition);
    }

    public void leftPress(Viewport mousePosition) {
        updateFocussedElement(mousePosition);
        elementUnderMouse.press();
//        displayInfo();
        setSomethingInMotion();
        state.leftPress(mousePosition);
    }

    public void mouseRelease() {
        state.unpress();
        elementUnderMouse.unpress();
        cameraInMotion = false;
        elementInMotion = false;
    }

    public void rightPress(Viewport mousePosition) {
        state.rightPress(mousePosition);
    }

    public void rightClick(Viewport mousePosition) {
        state.rightClick(mousePosition);
    }

    public void leftClick(Viewport mousePosition) {
        updateCoordinates(mousePosition);
        state.leftClick(mousePosition);
    }

    public void processKeyPressed(KeyEvent k) {
        if (textEditing) {
            textInEdition.processKeyPressed(k);
        }
    }

    public void rightArrow() {
        if (textEditing) {
            textInEdition.moveCursorRight();
        }
    }

    public void leftArrow() {
        if (textEditing) {
            textInEdition.moveCursorLeft();
        }
    }

    public void setSelectionAppend(boolean isControlDown) {
        if (appending != isControlDown) {
            appending = isControlDown;
        }
    }

    public void saveSimulation() {

    }

    public void processImageSelection() {
        if (onNetworkEditableElement()) {
            SimulationImage selectedElement = (SimulationImage) elementUnderMouse;
            if (selectedElements.contains(selectedElement)) {
                if (appending) {
                    selectedElement.unselect();
                    selectedElements.remove(selectedElement);
                } else {
                    emptySelection();
                    selectedElement.select();
                    selectedElements.add(selectedElement);
                }
            } else if (appending) {
                selectedElement.select();
                selectedElements.add(selectedElement);
            } else {
                emptySelection();
                selectedElement.select();
                selectedElements.add(selectedElement);
            }
        }
        if (onBackground() && !appending) {
            emptySelection();
        }
        closeEditionBox();
        if (onlyOneSelected()) {
            openNetworkEditionBoxes();
        } else {
            removeNetworkEditionBoxes();
        }
        if (selectedElements.isEmpty()) {
            removeCornerDeleteButton();
        }
    }

    public void emptySelection() {
        for (int i = selectedElements.size() - 1; i >= 0; i--) {
            selectedElements.get(i).unselect();
        }
        if (textEditing) {
            endEdition();
        }
        selectedElements.clear();
        removeNetworkEditionBoxes();
        removeGatheringBoxes();
        removeCornerDeleteButton();
    }

    public boolean onlyOneSelected() {
        return selectedElements.size() == 1;
    }

    public void zoom(double precisionWheelRotation) {
        if (precisionWheelRotation < 0) {
            for (int i = (int) precisionWheelRotation; i != 0; i++) {
                workPlane.zoomIn();
            }
        } else {
            for (int i = (int) precisionWheelRotation; i != 0; i--) {
                workPlane.zoomOut();
            }
        }
        statusBar.updateZoomRatio(workPlane.getZoomRatio());
    }

    /**
     * Movements
     *
     * @param mousePosition
     */
    public void moveSomething(Viewport mousePosition) {
        if (cameraInMotion) {
            double dragX = cursorX - mousePosition.getX();
            double dragY = cursorY - mousePosition.getY();
            Viewport newPostion = workPlane.getMousePositionOnMap(new Viewport(dragX, dragY));
            Coordinates coordinates = Coordinates.getCoordinatesFromXY(newPostion.toPoint());
            workPlane.moveCameraTo(coordinates);
            cursorX = mousePosition.getX();
            cursorY = mousePosition.getY();
        }
        if (elementInMotion) {
            Viewport newPostion = workPlane.getMousePositionOnMap(new Viewport(mousePosition.getX(), mousePosition.getY()));
            Coordinates coordinates = Coordinates.getCoordinatesFromXY(newPostion.toPoint());
            workPlane.moveNodeBy(coordinates);
            updateCoordinates(mousePosition);
        }

    }

    private void setSomethingInMotion() {
        if (onASelectedElement()) {
            elementInMotion = true;
            cameraInMotion = false;
            startElementMotion();
        }
        if (!elementInMotion && onBackground()) {
            cameraInMotion = true;
        }
    }

    private void startElementMotion() {
        fetchAllNodesInSelection();
        Viewport newPostion = workPlane.getMousePositionOnMap(new Viewport(0, 0));
        Coordinates coordinates = Coordinates.getCoordinatesFromXY(newPostion.toPoint());
        workPlane.startNodeMotion(nodesToMove, coordinates);
    }

    private void fetchAllNodesInSelection() {
        ArrayList<NodeImage> nodes = new ArrayList<>();
        for (SimulationImage selectedElement : selectedElements) {
            if (isNode(selectedElement)) {
                addNodeToArray(nodes, (NodeImage) selectedElement);
            }
        }

        SegmentImage segment;
        for (SimulationImage selectedElement : selectedElements) {
            if (isSegment(selectedElement)) {
                segment = (SegmentImage) selectedElement;
                addNodeToArray(nodes, segment.getOrigin());
                addNodeToArray(nodes, segment.getEnd());
            }
        }
        nodesToMove = nodes;
    }

    private void addNodeToArray(ArrayList<NodeImage> nodesToMove, NodeImage origin) {
        if (!nodesToMove.contains(origin)) {
            nodesToMove.add(origin);
        }
    }

    public void stopElementMotion() {
        if (onlyOneSelected()) {
            closeEditionBox();
            openNetworkEditionBoxes();
        }
        workPlane.stopNodeMotion();
    }

    public void saveIdAndDeleteImages() {
        inSelectionSegmentsIDs = new ArrayList<>();
        selectedStationIDs = new ArrayList<>();
        selectedIntersectionIDs = new ArrayList<>();
        concernedBySelectionTransitsIDs = new ArrayList<>();

        for (SimulationImage selectedElement : selectedElements) {
            if (isSegment(selectedElement)) {
                addSegmentForDelete((SegmentImage) selectedElement);
            } else if (isNode(selectedElement)) {
                addNodeForDelete((NodeImage) selectedElement);
            }
        }
        if (selectedTransit instanceof CircuitImage) {
            ArrayList<Integer> transportNeedIDs = getTransportNeedIDsThatUsesThatCircuit(selectedTransit.getIdentifier());
            for (Integer transportNeedID : transportNeedIDs) {
                if (!concernedBySelectionTransitsIDs.contains(transportNeedID)) {
                    concernedBySelectionTransitsIDs.add(transportNeedID);
                }
            }
        }
        emptySelection();
    }

    private void addNodeForDelete(NodeImage node) {
        addLinkedSegmentForDelete(node);
        if (isStation(node)) {
            if (!selectedStationIDs.contains(node.getIdentifier())) {
                selectedStationIDs.add(node.getIdentifier());
            }
        } else if (isIntersection(node)) {
            if (!selectedIntersectionIDs.contains(node.getIdentifier())) {
                selectedIntersectionIDs.add(node.getIdentifier());
            }
        }
    }

    private void addLinkedSegmentForDelete(NodeImage node) {
        ArrayList<SegmentImage> linkedSegments = new ArrayList<>(node.getLinkedSegments());
        for (int i = linkedSegments.size() - 1; i >= 0; i--) {
            addSegmentForDelete(linkedSegments.get(i));
        }
    }

    private void addSegmentForDelete(SegmentImage segment) {
        if (!inSelectionSegmentsIDs.contains(segment.getIdentifier())) {
            inSelectionSegmentsIDs.add(segment.getIdentifier());
            if (segment.isTwoWay()) {
                inSelectionSegmentsIDs.add(segment.getSecondWayId());
            }
        }
        addImpactedTransitsForDelete(segment);
    }

    private void addImpactedTransitsForDelete(SegmentImage segment) {
        for (Integer transitID : segment.getTransitList()) {
            if (!concernedBySelectionTransitsIDs.contains(transitID)) {
                concernedBySelectionTransitsIDs.add(transitID);
            }
        }
        for (Integer transitID : segment.getReverseTransitList()) {
            if (!concernedBySelectionTransitsIDs.contains(transitID)) {
                concernedBySelectionTransitsIDs.add(transitID);
            }
        }
    }

    /**
     * A segment in selection might be directly selected, or can be unselected
     * and attached to a selected node.
     *
     * @return an ArrayList containing the IDs the segments in selection.
     */
    public ArrayList<Integer> getInSelectionSegmentID() {
        return inSelectionSegmentsIDs;
    }

    public ArrayList<Integer> getSelectedIntersectionID() {
        return selectedIntersectionIDs;
    }

    public ArrayList<Integer> getSelectedStationID() {
        return selectedStationIDs;
    }

    public int getSelectedCircuitID() {
        return selectedCircuitID;
    }

    public int getSelectedTransportNeedID() {
        return selectedTransportNeedID;
    }

    public ArrayList<Integer> getConcernedBySelectionTransitID() {
        return concernedBySelectionTransitsIDs;
    }

    public Coordinates getCurrentCoordinates() {
        return currentCoordinates;
    }

    public void loadText() {
        textInEdition = (Text) elementUnderMouse;
        textInEdition.backup();
        textInEdition.press();
        textEditing = true;
    }

    public void endEdition() {
        if (textEditing) {
            elementModified = textInEdition.hasChanged();
            if (elementModified) {
                saveModifiedContent();
            }
            textInEdition.unpress();
            textEditing = false;
        }
    }

    public boolean isElementModified() {
        if (elementModified) {
            elementModified = false;
            return true;
        } else {
            return false;
        }
    }

    public boolean isAnimationModified() {
        if (animationModified) {
            animationModified = false;
            return true;
        } else {
            return false;
        }
    }

    public int storeCircuit() {
        Displayable circuitSummary = circuitChooser.findDirectClick(cursorPosition);
        int transitID = -1;
        if (isCircuitChoice(circuitSummary)) {
            transitID = ((CircuitChooser.CircuitChoice) circuitSummary).getElementID();
            selectedCircuit = (CircuitImage) workPlane.getSimulationImage(String.valueOf(transitID));
            selectedCircuitID = transitID;
        }
        return transitID;

    }

    public int storeTransit() {
        Displayable transitSummay = transitGatheringBox.findDirectClick(cursorPosition);
        int transitID = -1;
        if (isTransitSummaryBox(transitSummay)) {
            transitID = ((TransitSummaryBox) transitSummay).getElementID();
            selectedTransit = (TransitImage) workPlane.getSimulationImage(String.valueOf(transitID));
            if (isCircuit(selectedTransit)) {
                selectedCircuitID = transitID;
                selectedTransportNeedID = -1;
            } else if (isTransportNeed(selectedTransit)) {
                selectedCircuitID = -1;
                selectedTransportNeedID = transitID;
            }
        }
        return transitID;
    }

    private void saveModifiedContent() {
        modifiedElement = null;
        modifiedSecondWay = null;
        if (networkEditionBox.isAnimationParametersOpen()) {
            currentAnimation = networkEditionBox.getParametrisedAnimation();
            animationModified = true;
        } else {
            SimulationImage editedElement = workPlane.getSimulationImage(String.valueOf(networkEditionBox.getElementInEditionID()));

            System.out.println(editedElement.getClass().getSimpleName());
            if (isStation(editedElement)) {
                modifiedElement = networkEditionBox.getModifiedStation();
            } else if (isIntersection(editedElement)) {
                modifiedElement = networkEditionBox.getModifiedIntersection();
            } else if (isSegment(editedElement)) {
                modifiedElement = networkEditionBox.getModifiedSegment();
                SegmentImage editedSegment = (SegmentImage) editedElement;
                if (editedSegment.isTwoWay()) {
                    modifiedSecondWay = networkEditionBox.getSecondSegment();
                }
            } else if (isCircuit(editedElement)) {
                modifiedElement = networkEditionBox.getModifiedCircuit();
            } else if (isTransportNeed(editedElement)) {
                modifiedElement = networkEditionBox.getModifiedTransportNeed();
            }
        }

    }

    public boolean areTransportNeedAppended() {
        return !transitGatheringBox.isAppendingCircuit();
    }

    public boolean areCircuitAppended() {
        return transitGatheringBox.isAppendingCircuit();
    }

    public SimulationElement getModifiedElement() {
        return modifiedElement;
    }

    public boolean isThereASecondSegment() {
        return modifiedSecondWay != null;
    }

    public Segment getModifiedSecondWay() {
        int origin = modifiedSecondWay.getOrigin();
        int end = modifiedSecondWay.getEnd();
        String transferName = modifiedSecondWay.getName();
        int id = modifiedSecondWay.getIdentifier();
        TriangularDistribution transferDistribution = modifiedSecondWay.getTriangularDistribution();
        Segment transferSegment = new Segment(transferName, id, origin, end, transferDistribution);
        modifiedSecondWay = null;
        return transferSegment;
    }

    public AnimatorImage getParametrisedAnimation() {
        return currentAnimation;
    }

    public void startAnimation() {
        emptySelection();
        animationTools.fold();
        topMenu.fold();
        workPlane.startAnimation(animationControlBar);
        addChild(animationControlBar);
    }

    public String getAnimationReport() {
        return workPlane.getReport();
    }

    public void pauseAnimation() {
        workPlane.pauseAnimation();
    }

    public void restartReplication() {
        workPlane.restartReplication();
    }

    public void forwardAnHour() {
        workPlane.forwardAnHour();
    }

    public void backAnHour() {
        workPlane.backAnHour();
    }

    public void nextReplication() {
        workPlane.nextReplication();
    }

    public void previousReplication() {
        workPlane.previousReplication();
    }

    public void faster() {
        workPlane.faster();
    }

    public void slower() {
        workPlane.slower();
    }

    public void setClickedTime() {
        double clickedTime = animationControlBar.getProgressionFromClickAt(cursorPosition.getX());
        workPlane.setClickedTime(clickedTime);
    }

    public void playAnimation() {
        workPlane.playAnimation();
    }

    public void stopAnimation() {
        workPlane.stopAnimation();
        removeChild(animationControlBar);
    }

    /**
     * This paint method might look stupid, but it is done as such to avoid
     * concurrent fail-fast errors.
     */
    @Override
    public void paint(Graphics2D graphics2D) {
        paintIfPresent(graphics2D, workPlane);
        paintIfPresent(graphics2D, statusBar);
        paintIfPresent(graphics2D, topMenu);
        paintIfPresent(graphics2D, animationTools);
        paintIfPresent(graphics2D, cornerDeleteMenu);
        paintIfPresent(graphics2D, pathEditionChoices);
        paintIfPresent(graphics2D, networkEditionBox);
        paintIfPresent(graphics2D, transitGatheringBox);
        paintIfPresent(graphics2D, circuitChooser);
        paintIfPresent(graphics2D, contextualCreationMenu);
        if (currentCursor != null) {
            BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorImg, new Point(0, 0), "blank cursor");
            Toolkit.getDefaultToolkit().createCustomCursor(currentCursor, new Point(0, 0), name);
            graphics2D.drawImage(currentCursor, (int) cursorX, (int) cursorY, null);
        }
    }

    private void paintIfPresent(Graphics2D graphics2D, Box element) {
        if (getContainedElement().containsValue(element)) {
            element.paint(graphics2D);
        }
    }

    private void displayInfo() {
        System.out.println("\n--------------------------------------------------------------"
                + "\nSource : ");
        System.out.println(elementUnderMouse.getClass().getSimpleName() + " : " + elementUnderMouse.getName());
        System.out.println("Position : (" + cursorX + ", " + cursorY + ")");
        System.out.println("State : " + state.getClass().getSimpleName());
    }

    @Override
    public void setRenderProperties(Viewport viewport) {
    }

    public void addElementToSelected(Displayable displayable) {
        focussedElement = displayable;
    }

    public boolean onASelectedElement() {
        boolean onElement = false;
        if (onNetworkEditableElement()) {
            onElement = selectedElements.contains((SimulationImage) elementUnderMouse);
        }
        return onElement;
    }

    public boolean onStation() {
        return isMouseOn(STATION_IMAGE);
    }

    public boolean onIntersection() {
        return isMouseOn(INTERSECTION_IMAGE);
    }

    public boolean onNode() {
        return isMouseOn(INTERSECTION_IMAGE) || isMouseOn(STATION_IMAGE);
    }

    public boolean onNetworkEditableElement() {
        return onSegment() || onStation() || onIntersection();
    }

    public boolean onSegment() {
        return isMouseOn(SEGMENT_IMAGE);
    }

    public boolean onBackground() {
        return elementUnderMouse == workPlane;
    }

    public boolean onBackground(Viewport position) {
        return workPlane.findHighestClick(position) == workPlane;
    }

    public boolean onEditableText() {
        return isMouseOn(Text.class.getSimpleName()) && elementUnderMouse.isEditable();
    }

    public boolean onCircuitButton() {
        return isMouseOn(CircuitButton.class.getSimpleName());
    }

    public boolean onTransportNeedButton() {
        return isMouseOn(TransportNeedButton.class.getSimpleName());
    }

    public boolean onChannelUp() {
        return isMouseOn(ChannelUp.class.getSimpleName());
    }

    public boolean onChannelDown() {
        return isMouseOn(ChannelDown.class.getSimpleName());
    }

    public boolean onChannelRight() {
        return isMouseOn(ChannelRight.class.getSimpleName());
    }

    public boolean onChannelLeft() {
        return isMouseOn(ChannelLeft.class.getSimpleName());
    }

    public boolean onOpenFile() {
        return isMouseOn(OpenFile.class.getSimpleName());
    }

    public boolean onSave() {
        return isMouseOn(Save.class.getSimpleName());
    }

    public boolean onUndo() {
        return isMouseOn(Undo.class.getSimpleName()) || isMouseOn(CircularUndo.class.getSimpleName());
    }

    public boolean onRedo() {
        return isMouseOn(Redo.class.getSimpleName());
    }

    public boolean onEarth() {
        return isMouseOn(Earth.class.getSimpleName());
    }

    public boolean onQuit() {
        return isMouseOn(Quit.class.getSimpleName());
    }

    public boolean onAnimationParameters() {
        return isMouseOn(AnimationParameters.class.getSimpleName());
    }

    public boolean onStartAnimation() {
        return isMouseOn(StartAnimation.class.getSimpleName());
    }

    public boolean onCancel() {
        return isMouseOn(SquareCancelButton.class.getSimpleName()) || isMouseOn(CancelButton.class.getSimpleName());
    }

    public boolean onConfirm() {
        return isMouseOn(SquareConfirmButton.class.getSimpleName()) || isMouseOn(ConfirmButton.class.getSimpleName());
    }

    public boolean onEditTransit() {
        return isMouseOn(EditTransit.class.getSimpleName());
    }

    public boolean onDelete() {
        return isMouseOn(SquareDeleteButton.class.getSimpleName()) || isMouseOn(DeleteButton.class.getSimpleName());
    }

    public boolean onPartialDelete() {
        return isMouseOn(PartialDelete.class.getSimpleName());
    }

    public boolean onPlay() {
        return isMouseOn(Play.class.getSimpleName());
    }

    public boolean onPause() {
        return isMouseOn(Pause.class.getSimpleName());
    }

    public boolean onStop() {
        return isMouseOn(Stop.class.getSimpleName());
    }

    public boolean onRestart() {
        return isMouseOn(Restart.class.getSimpleName());
    }

    public boolean onForward() {
        return isMouseOn(Forward.class.getSimpleName());
    }

    public boolean onRewind() {
        return isMouseOn(Rewind.class.getSimpleName());
    }

    public boolean onNextReplication() {
        return isMouseOn(NextReplication.class.getSimpleName());
    }

    public boolean onRabbit() {
        return isMouseOn(Rabbit.class.getSimpleName());
    }

    public boolean onTurtle() {
        return isMouseOn(Turtle.class.getSimpleName());
    }

    public boolean onPreviousReplication() {
        return isMouseOn(PreviousReplication.class.getSimpleName());
    }

    public boolean onProgressBar() {
        return isMouseOn(ProgressBar.class.getSimpleName());
    }

    private boolean isMouseOn(String clickedOn) {
        return elementUnderMouse.getClass().getSimpleName().equals(clickedOn);
    }

    public boolean isStation(Displayable element) {
        return element.getClass().getSimpleName().equals(STATION_IMAGE);
    }

    public boolean isIntersection(Displayable element) {
        return element.getClass().getSimpleName().equals(INTERSECTION_IMAGE);
    }

    public boolean isSegment(Displayable element) {
        return element.getClass().getSimpleName().equals(SEGMENT_IMAGE);
    }

    public boolean isAnimation(Displayable element) {
        return element.getClass().getSimpleName().equals(ANIMATION_IMAGE);
    }

    public boolean isCircuit(Displayable element) {
        return element.getClass().getSimpleName().equals(CIRCUIT_IMAGE);
    }

    public boolean isTransportNeed(Displayable element) {
        return element.getClass().getSimpleName().equals(TRANSPORT_NEED_IMAGE);
    }

    private boolean isTransitSummaryBox(Displayable element) {
        return element.getClass().getSimpleName().equals(TRANSIT_SUMMARY_BOX);
    }

    public boolean isCircuitChoice(Displayable element) {
        return element.getClass().getSimpleName().equals(CIRCUIT_CHOICE);
    }

    public boolean isNode(SimulationImage selectedElement) {
        return isStation(selectedElement) || isIntersection(selectedElement);
    }

    private boolean onThisDisplayable(Displayable displayable) {
        return findHighestClick(cursorPosition) == displayable.findHighestClick(cursorPosition);
    }

    public boolean onContextualCreationMenu() {
        return onThisDisplayable(contextualCreationMenu);
    }

    public boolean onPathEditionChoices() {
        return onThisDisplayable(pathEditionChoices);
    }

    public boolean onAddStation() {
        return isMouseOn(AddStationButton.class.getSimpleName());
    }

    public boolean onAddIntersection() {
        return isMouseOn(AddIntersectionButton.class.getSimpleName());
    }

    public boolean onAddSegment() {
        return isMouseOn(AddSegmentButton.class.getSimpleName());
    }

    public boolean onAdd() {
        return isMouseOn(Add.class.getSimpleName());
    }

    public boolean onNetworkEditionBox() {
        return onThisDisplayable(networkEditionBox);
    }

    public boolean onTransitGatheringBox() {
        return onThisDisplayable(transitGatheringBox);
    }

    public boolean onCircuitChooser() {
        return onThisDisplayable(circuitChooser);
    }

    public boolean onWorkplane() {
        return onThisDisplayable(workPlane);
    }

    public boolean onCreationMenu() {
        return onThisDisplayable(contextualCreationMenu);
    }

    public boolean onTopMenu() {
        return onThisDisplayable(topMenu);
    }

    public boolean onAnimationTools() {
        return onThisDisplayable(animationTools);
    }

    public boolean onAnimationControlBar() {
        return onThisDisplayable(animationControlBar);
    }

    public Displayable getClickedOnElement() {
        return elementUnderMouse;
    }

    public NodeImage getNode(Viewport mousePosition) {
        return workPlane.getNodeAt(mousePosition);
    }

    public SegmentImage getSegment(Viewport mousePosition) {
        return workPlane.getSegmentAt(mousePosition);
    }

    public StationImage getStation(Viewport mousePosition) {
        return workPlane.getStationAt(mousePosition);
    }

    public IntersectionImage getIntersection(Viewport mousePosition) {
        return workPlane.getIntersectionAt(mousePosition);
    }

    public int getSelectedElementID() {
        SimulationImage image = (SimulationImage) elementUnderMouse;
        return image.getIdentifier();
    }

    public ArrayList<Integer> getTransportNeedIDsThatUsesThatCircuit(int circuitID) {
        return workPlane.getTransportNeedIDsThatUsesThatCircuit(circuitID);
    }

    public boolean isElementInMotion() {
        return elementInMotion;
    }

    public ArrayList<NodeImage> getNodesToMove() {
        return nodesToMove;
    }

    public void updateRendering() {
        workPlane.setChange();
    }

    public void refreshElementsAfterModification() {
        workPlane.setChangedElement();
    }

    public boolean isTextEditing() {
        return textEditing;
    }

    public void setDisplayMap(boolean status) {
        workPlane.setDisplayMap(status);
    }

    public boolean getDisplayMap() {
        return workPlane.getDisplayMap();
    }

}
