/*
 * GNU General Public License v2
 *
 * @version $Id: MenuTest.java 261 2009-07-05 04:13:37Z ru.energy $
 */
package jtanks.game.screens.helpers;

import jtanks.game.screens.Screen;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {

    public MenuTest() {
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
     * Test of add method, of class Menu.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        MenuItem item = null;
        Menu instance = new Menu();
        instance.add(item);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSelected method, of class Menu.
     */
    @Test
    public void testGetSelected() {
        System.out.println("getSelected");
        Menu instance = new Menu();
        MenuItem expResult = null;
        MenuItem result = instance.getSelected();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of selectNext method, of class Menu.
     */
    @Test
    public void testSelectNext() {
        System.out.println("selectNext");
        Menu instance = new Menu();
        instance.selectNext();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of selectPrevious method, of class Menu.
     */
    @Test
    public void testSelectPrevious() {
        System.out.println("selectPrevious");
        Menu instance = new Menu();
        instance.selectPrevious();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of selectedIndex method, of class Menu.
     */
    @Test
    public void testSelectedIndex() {
        System.out.println("selectedIndex");
        Menu instance = new Menu();
        int expResult = 0;
        int result = instance.selectedIndex();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getItem method, of class Menu.
     */
    @Test
    public void testGetItem() {
        System.out.println("getItem");
        byte b = 0;
        Menu instance = new Menu();
        MenuItem expResult = null;
        MenuItem result = instance.getItem(b);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of select method, of class Menu.
     */
    @Test
    public void testSelect_MenuItem() {
        System.out.println("select");
        MenuItem item = null;
        Menu instance = new Menu();
        instance.select(item);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of select method, of class Menu.
     */
    @Test
    public void testSelect_int() {
        System.out.println("select");
        int index = 0;
        Menu instance = new Menu();
        instance.select(index);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toArray method, of class Menu.
     */
    @Test
    public void testToArray() {
        System.out.println("toArray");
        Menu instance = new Menu();
        MenuItem[] expResult = null;
        MenuItem[] result = instance.toArray();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCaller method, of class Menu.
     */
    @Test
    public void testSetCaller() {
        System.out.println("setCaller");
        Screen caller = null;
        Menu instance = new Menu();
        instance.setCaller(caller);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of size method, of class Menu.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        Menu instance = new Menu();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}