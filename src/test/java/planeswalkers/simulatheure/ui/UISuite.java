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
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import planeswalkers.simulatheure.ui.components.ComponentsSuite;
import planeswalkers.simulatheure.ui.gui.GUISuite;

/**
 *
 * @author Jean-Benoît
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ComponentsSuite.class, GUISuite.class, AssetManagerTest.class})
public class UISuite {

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
