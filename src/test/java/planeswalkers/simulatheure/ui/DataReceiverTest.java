/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Jean-Benoît
 */
public class DataReceiverTest {

    public DataReceiverTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

//    @Test
//    public void testGetNetworkElements() {
//        DataReceiver receiver = new DataReceiver();
//        HashMap<String, HashMap<Integer, SimulationElement>> expectedInformations = new HashMap<>();
//        //HashMap<String, HashMap<Integer, SimulationElement>> result = receiver.getSimulationElements();
//        //assertThat(result, equalTo(expectedInformations));
//    }
//
//    @Test
//    public void testUpdateNetworkElements() {
//        DataReceiver receiver = new DataReceiver();
//        HashMap<String, HashMap<Integer, SimulationElement>> expectedInformations = new HashMap<>();
//        HashMap<Integer, SimulationElement> elements = new HashMap<>();
//        Coordinates coorJB = new Coordinates(46.7737452, -71.2866883, 0);
//        Station stationJB = new Station("JB's", 1, coorJB);
//
//        elements.put(1, stationJB);
//        expectedInformations.put("Station", elements);
//
//        receiver.updateSimulationElements(expectedInformations);
//
//        //HashMap<String, HashMap<Integer, SimulationElement>> result = receiver.getSimulationElements();
//        //assertThat(result, equalTo(expectedInformations));
//    }
//
//    @Test
//    public void testUpdateNetworkElements_anIntersectionIsAdded() {
//        DataReceiver receiver = new DataReceiver();
//        NetworkElementFactory RTC = new NetworkElementFactory("RTC");
//        Coordinates coorJB = new Coordinates(46.7737452, -71.2866883, 0);
//        RTC.createIntersection(coorJB);
//        assertThat(receiver.getSimulationElements(), IsMapContaining.hasKey(Intersection.class.getSimpleName()));
//    }
//    @Test
//    public void testGetNetworkElements() {
//        DataReceiver receiver = new DataReceiver();
//        HashMap<String, HashMap<Integer, SimulationElement>> expectedInformations = new HashMap<>();
//        //HashMap<String, HashMap<Integer, SimulationElement>> result = receiver.getSimulationElements();
//        //assertThat(result, equalTo(expectedInformations));
//    }
//
//    @Test
//    public void testUpdateNetworkElements() {
//        DataReceiver receiver = new DataReceiver();
//        HashMap<String, HashMap<Integer, SimulationElement>> expectedInformations = new HashMap<>();
//        HashMap<Integer, SimulationElement> elements = new HashMap<>();
//        Coordinates coorJB = new Coordinates(46.7737452, -71.2866883, 0);
//        Station stationJB = new Station("JB's", 1, coorJB);
//
//        elements.put(1, stationJB);
//        expectedInformations.put("Station", elements);
//
//        receiver.updateSimulationElements(expectedInformations);
//
//        //HashMap<String, HashMap<Integer, SimulationElement>> result = receiver.getSimulationElements();
//        //assertThat(result, equalTo(expectedInformations));
//    }

    /*@Test
     public void testUpdateNetworkElements_anIntersectionIsAdded() {
     DataReceiver receiver = new DataReceiver();
     NetworkElementFactory RTC = new NetworkElementFactory("RTC");
     Coordinates coorJB = new Coordinates(46.7737452, -71.2866883, 0);
     RTC.createIntersection(coorJB);
     assertThat(receiver.getSimulationElements(), IsMapContaining.hasKey(Intersection.class.getSimpleName()));
     }*/
}
