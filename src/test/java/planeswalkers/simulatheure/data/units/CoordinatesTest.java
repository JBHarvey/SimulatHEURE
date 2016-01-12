/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.units;

import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Jean-Benoît
 */
public class CoordinatesTest {

    Coordinates coordinates;

    public CoordinatesTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        coordinates = new Coordinates(40.0, -70.0, 0.0);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetLongitude() {
        Assert.assertThat(coordinates.getLongitude(), equalTo(-70.0));
    }

    @Test
    public void testGetLatitude() {
        Assert.assertThat(coordinates.getLatitude(), equalTo(40.0));
    }
}
