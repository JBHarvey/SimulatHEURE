/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.network;

import planeswalkers.simulatheure.data.networkelement.Station;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import planeswalkers.simulatheure.data.units.Coordinates;

/**
 *
 * @author Jean-Benoît
 */
public class NetworkElementTest {

    public NetworkElementTest() {
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
    public void testGetIdentifier() {
        Station s = new Station("Station1", 1, new Coordinates(0, 0, 0));
        assertThat(s.getIdentifier(), equalTo(1));
    }
}
