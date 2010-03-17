/*
 * GNU General Public License v2
 *
 * @version $Id: DisplayManagerTest.java 261 2009-07-05 04:13:37Z ru.energy $
 */
package jtanks.system;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DisplayManagerTest {

    public DisplayManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getDisplayMode method, of class DisplayManager.
     */
    @Test
    public void testGetDisplayMode() {
        DisplayManager instance = new DisplayManager();
        DisplayMode expResult = null;
        DisplayMode result = instance.getDisplayMode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDisplayMode method, of class DisplayManager.
     */
    @Test
    public void testSetDisplayMode() {
        DisplayMode mode = null;
        DisplayManager instance = new DisplayManager();
        instance.setDisplayMode(mode);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of restoreDisplay method, of class DisplayManager.
     */
    @Test
    public void testRestoreDisplay() {
        DisplayManager instance = new DisplayManager();
        instance.restoreDisplay();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSystemDisplayMode method, of class DisplayManager.
     */
    @Test
    public void testGetSystemDisplayMode() {
        DisplayManager instance = new DisplayManager();
        DisplayMode expResult = null;
        DisplayMode result = instance.getSystemDisplayMode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isDisplayChangeSupported method, of class DisplayManager.
     */
    @Test
    public void testIsDisplayChangeSupported() {
        DisplayManager instance = new DisplayManager();
        boolean expResult = false;
        boolean result = instance.isDisplayChangeSupported();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDisplayModes method, of class DisplayManager.
     */
    @Test
    public void testGetDisplayModes() {
        DisplayManager instance = new DisplayManager();
        DisplayMode[] expResult = null;
        DisplayMode[] result = instance.getDisplayModes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCurrentDisplayMode method, of class DisplayManager.
     */
    @Test
    public void testGetCurrentDisplayMode() {
        DisplayManager instance = new DisplayManager();
        DisplayMode expResult = null;
        DisplayMode result = instance.getCurrentDisplayMode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGraphicsDevice method, of class DisplayManager.
     */
    @Test
    public void testGetGraphicsDevice() {
        DisplayManager instance = new DisplayManager();
        GraphicsDevice expResult = null;
        GraphicsDevice result = instance.getGraphicsDevice();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isFullScreen method, of class DisplayManager.
     */
    @Test
    public void testIsFullScreen() {
        DisplayManager instance = new DisplayManager();
        boolean expResult = false;
        boolean result = instance.isFullScreen();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}