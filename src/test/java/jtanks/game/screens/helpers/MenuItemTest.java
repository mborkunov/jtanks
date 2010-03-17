/*
 * GNU General Public License v2
 *
 * @version $Id: MenuItemTest.java 261 2009-07-05 04:13:37Z ru.energy $
 */
package jtanks.game.screens.helpers;

import jtanks.game.screens.Screen;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuItemTest {

    public MenuItemTest() {
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
     * Test of setCaller method, of class MenuItem.
     */
    @Test
    public void testSetCaller() {
        Screen caller = null;
        MenuItem instance = null;
        instance.setCaller(caller);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getScreen method, of class MenuItem.
     */
    @Test
    public void testGetScreen() throws Exception {
        MenuItem instance = null;
        Screen expResult = null;
        Screen result = instance.getScreen();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setData method, of class MenuItem.
     */
    @Test
    public void testSetData() {
        DataTransfer data = null;
        MenuItem instance = null;
        //instance.setData(data);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of select method, of class MenuItem.
     */
    @Test
    public void testSelect() {
        boolean status = false;
        MenuItem instance = null;
        instance.select(status);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSelected method, of class MenuItem.
     */
    @Test
    public void testIsSelected() {
        MenuItem instance = null;
        boolean expResult = false;
        boolean result = instance.isSelected();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isActive method, of class MenuItem.
     */
    @Test
    public void testIsActive() {
        MenuItem instance = null;
        boolean expResult = false;
        boolean result = instance.isActive();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setActive method, of class MenuItem.
     */
    @Test
    public void testSetActive() {
        boolean active = false;
        MenuItem instance = null;
        instance.setActive(active);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class MenuItem.
     */
    @Test
    public void testToString() {
        MenuItem instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}