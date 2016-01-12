/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import planeswalkers.simulatheure.data.network.NetworkSuite;
import planeswalkers.simulatheure.data.units.UnitsSuite;

/**
 *
 * @author Marc-Antoine Fortier
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({NetworkSuite.class, UnitsSuite.class})
public class DataSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

}
