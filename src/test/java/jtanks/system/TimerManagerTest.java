/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.system;

import java.util.Timer;
import java.util.TimerTask;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerManagerTest {

    public TimerManagerTest() {
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
     * Test of createTimer method, of class TimerManager.
     */
    @Test
    public void testCreateTimer() {
        String name = "";
        TimerTask task = null;
        long delay = 0L;
        TimerManager instance = new TimerManager();
        boolean expResult = false;
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTimer method, of class TimerManager.
     */
    @Test
    public void testGetTimer() {
        String name = "";
        TimerManager instance = new TimerManager();
        Timer expResult = null;
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeTimer method, of class TimerManager.
     */
    @Test
    public void testRemoveTimer() {
        String name = "";
        TimerManager instance = new TimerManager();
        Timer expResult = null;
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}