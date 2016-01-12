/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.ui.gui;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import planeswalkers.simulatheure.data.units.Coordinates;
import planeswalkers.simulatheure.ui.DataReceiver;

/**
 *
 * @author Marc-Antoine Fortier
 */
public class CameraTest {

    private static final double ZOOM_STEP = 0.1;
    private static final double MOVE_STEP = 0.0000001f;
    private final double initialZoom;
    private final Coordinates initialCoordinates;
    private Camera cam;
    private DataReceiver dataReceiver;

    public CameraTest() {
        initialZoom = 1;
        initialCoordinates = new Coordinates(46.7830306, -71.2769762, 0);
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        cam = new Camera(dataReceiver);
    }

    @After
    public void tearDown() {
    }
    /*
     @Test
     public void testGetZoomRatio() {
     assertThat(cam.zoomRatio, isA(Double.class));
     }

     @Test
     public void testZoomIn() {
     cam.zoomIn();
     assertThat(cam.zoomRatio, equalTo(initialZoom + ZOOM_STEP));
     }

     @Test
     public void testZoomOut() {
     cam.zoomOut();
     assertThat(cam.zoomRatio, equalTo(initialZoom - ZOOM_STEP));
     }

     @Test
     public void testMoveEast() {
     cam.moveEast();
     assertThat(cam.longitude, equalTo(initialCoordinates.getLongitude() + MOVE_STEP));
     }

     @Test
     public void testMoveWest() {
     cam.moveWest();
     assertThat(cam.longitude, equalTo(initialCoordinates.getLongitude() - MOVE_STEP));
     }

     @Test
     public void testMoveNorth() {
     cam.moveNorth();
     assertThat(cam.latitude, equalTo(initialCoordinates.getLatitude() + MOVE_STEP));
     }

     @Test
     public void testMoveSouth() {
     cam.moveSouth();
     assertThat(cam.latitude, equalTo(initialCoordinates.getLatitude() - MOVE_STEP));
     }*/
}
