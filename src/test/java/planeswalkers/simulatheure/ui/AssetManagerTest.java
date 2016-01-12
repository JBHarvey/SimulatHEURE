/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui;

import com.kitfox.svg.SVGDiagram;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Benoît
 */
public class AssetManagerTest {

    final AssetManager instance;
    final String STATION = "Station";
    final String INTERSECTION = "Intersection";

    public AssetManagerTest() {
        instance = AssetManager.getInstance();
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

    /*@Test
    public void testGetInstance() {
        assertThat(instance, is(notNullValue()));
    }

    @Test
    public void testGetInstance_ReturnTheSameInstanceTwice() {
        assertThat(instance, is(sameInstance(AssetManager.getInstance())));
    }

    @Test
    public void testGetComponent() {
        assertThat(instance.getSimulationComponent(STATION), isA(SVGDiagram.class));
    }

    @Test
    public void testGetComponent_returnDifferentComponent_givenDifferentNames() {
        assertThat(instance.getSimulationComponent(STATION), not(instance.getSimulationComponent(INTERSECTION)));
    }

    @Test
    public void testGetComponent_returnDifferentComponent_givenSameName() {
        SVGDiagram diagram = instance.getSimulationComponent(STATION);
        assertThat(diagram, is(sameInstance(instance.getSimulationComponent(STATION))));
    }*/
}
