/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure;

import org.junit.runners.Suite;
import planeswalkers.simulatheure.data.DataSuite;
import planeswalkers.simulatheure.simulation.SimulationSuite;
import planeswalkers.simulatheure.ui.UISuite;

/**
 *
 * @author Jean-Benoît
 */
@org.junit.runner.RunWith(org.junit.runners.Suite.class)
@Suite.SuiteClasses({DataSuite.class, SimulationSuite.class, UISuite.class})
public class SimulatheureSuite {

    @org.junit.BeforeClass
    public static void setUpClass() throws Exception {
    }

    @org.junit.AfterClass
    public static void tearDownClass() throws Exception {
    }

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

}
