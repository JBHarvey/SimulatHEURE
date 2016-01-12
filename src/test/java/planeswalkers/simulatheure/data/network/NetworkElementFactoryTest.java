/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.network;

import java.util.HashMap;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.nullValue;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import planeswalkers.simulatheure.data.networkelement.Intersection;
import planeswalkers.simulatheure.data.networkelement.NetworkElementFactory;
import planeswalkers.simulatheure.data.units.Coordinates;

/**
 *
 * @author Jean-Benoit Harvey
 */
public class NetworkElementFactoryTest {

    final NetworkElementFactory RTC;
    final Coordinates coorJB = new Coordinates(46.7737452, -71.2866883, 0);
    final Coordinates coorDan = new Coordinates(46.780173, -71.489878, 0);

    /*
     final Coordinates coorMarc = new Coordinates(46.7887494, -71.2729422, 0);
     final Coordinates coorAntoine = new Coordinates(46.855059, -71.202857, 0);
     */

    public NetworkElementFactoryTest() {
        RTC = new NetworkElementFactory("RTC");
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
    /*
    @Test
    public void testGetNodes() {
        System.out.println("getNodes");
        System.out.println(RTC.getNodes());
        assertThat(RTC.getNodes(), isA(HashMap.class));
    }

//    @Test
//    public void testCreateStation() {
//        RTC.createStation(coorJB);
//        System.out.println(RTC.getNodes());
//        assertThat(RTC.getNodes().get(1), is(instanceOf(Station.class)));
//    }
//    @Test
//    public void testGetNodes_incrementalUniversalCounter() {
//
//        RTC.createStation(coorJB);
//        RTC.createStation(coorDan);
//        assertThat(RTC.getNodes().get(2), is(instanceOf(Station.class)));
//    }
//    @Test
//    public void testCreateIntersection() {
//        RTC.createIntersection(coorJB);
//        assertThat(RTC.getNodes().get(1), is(instanceOf(Intersection.class)));
//    }
    @Test
    public void testRemoveIntersection() {
        RTC.createIntersection(coorDan);
        RTC.removeIntersection(1);
        assertThat(RTC.getNodes().get(1), is(nullValue()));
    } */

    /**
     * Test of modifyStation method, of class NetworkElementFactory.
     */
    /* @Test
    public void testModifyStation_Station() {
        RTC.createStation(coorJB);
        Station stationDan = new Station("Daniel's", 1, coorDan);
        RTC.modifyNetworkElement(1, stationDan);
        assertThat(RTC.getNodes().get(1), is(instanceOf(Station.class)));
    }
     */
    /**
     * Test of modifyStation method, of class NetworkElementFactory.
     */
    /*  @Test
    public void testModifyStation_Intersection() {
        RTC.createStation(coorJB);
        Intersection intersectionDan = new Intersection("Daniel's", 1, coorDan);
        RTC.modifyNetworkElement(1, intersectionDan);
        assertThat(RTC.getNodes().get(1), is(instanceOf(Intersection.class)));
    }*/
}
