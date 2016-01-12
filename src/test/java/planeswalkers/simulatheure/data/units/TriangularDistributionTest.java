/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.units;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Antoine
 */
public class TriangularDistributionTest {

    public TriangularDistributionTest() {
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
    public void testComputeDistribution() {

        int min = 5;
        int max = 11;
        int avg = 7;
        TriangularDistribution t = new TriangularDistribution(min, max, avg);
        assertTrue(t.fetchComputation() >= min && t.fetchComputation() <= max);
    }
}
