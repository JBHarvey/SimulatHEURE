/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.network;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import planeswalkers.simulatheure.data.networkelement.Segment;
import planeswalkers.simulatheure.data.units.TriangularDistribution;

/**
 *
 * @author Antoine
 */
public class SegmentTest {

    public SegmentTest() {
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

    @Test
    public void testDrawFromDistribution() {
        TriangularDistribution td = new TriangularDistribution(2, 12, 8);
        Segment segment = new Segment("test", 2, 1, 1, td);
        segment.resetTransitTime();
        assertTrue(segment.fetchTransitTime() >= 1 && segment.fetchTransitTime() <= 12);
    }

}
