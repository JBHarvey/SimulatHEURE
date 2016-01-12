/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data.network;

import planeswalkers.simulatheure.data.networkelement.NetworkElementFactory;
import planeswalkers.simulatheure.data.NetworkFactory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import planeswalkers.simulatheure.ui.DataReceiver;

/**
 *
 * @author Marc-Antoine Fortier
 */
public class NetworkFactoryTest {

    final NetworkFactory instance;
    final DataReceiver receiver;
    final NetworkElementFactory RTC;

    public NetworkFactoryTest() {
        instance = NetworkFactory.getInstance();
        receiver = new DataReceiver();
        RTC = instance.getNetworkElementFactory("RTC");
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
    public void testGetInstance() {
        assertThat(instance, is(notNullValue()));
    }

    @Test
    public void testGetInstance_ReturnSameInstanceTwice() {
        assertThat(instance, is(sameInstance(NetworkFactory.getInstance())));
    }

    @Test
    public void testGetNetwork() {
        assertThat(RTC, isA(NetworkElementFactory.class));
    }

    @Test
    public void testGetNetwork_returnSameNetwork_givenSameName() {
        assertThat(RTC, is(sameInstance(RTC)));
    }

    @Test
    public void testGetNetwork_notSameInstance_notSameNames() {
        assertThat(RTC, not(instance.getNetworkElementFactory("STS")));
    }

}
