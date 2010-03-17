package jtanks.system;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OSTest {

    private String os;

    public OSTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        os = System.getProperty("os.name").toLowerCase();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isWindows method, of class OS.
     */
    @Test
    public void testIsWindows() {
        System.out.println("isWindows");

        if (os.contains("windows")) {
            assertTrue(OS.isWindows());
            assertFalse(OS.isMac());
            assertFalse(OS.isUnix());
        }
    }

    /**
     * Test of isMac method, of class OS.
     */
    @Test
    public void testIsMac() {
        System.out.println("isMac");

        if (os.contains("mac")) {
            assertTrue(OS.isMac());
            assertFalse(OS.isUnix());
            assertFalse(OS.isWindows());
        }
    }

    /**
     * Test of isUnix method, of class OS.
     */
    @Test
    public void testIsUnix() {
        System.out.println("isUnix");

        if (os.contains("nux") || os.contains("nix")) {
            assertTrue(OS.isUnix());
            assertFalse(OS.isMac());
            assertFalse(OS.isWindows());
        }
    }

}