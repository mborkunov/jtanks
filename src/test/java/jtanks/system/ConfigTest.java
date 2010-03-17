/*
 * GNU General Public License v2
 *
 * @version $Id: ConfigTest.java 291 2009-07-22 12:37:52Z ru.energy $
 */
package jtanks.system;

import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.System.out;

public class ConfigTest {
    private Config config;

    public ConfigTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        config = Config.getInstance();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of get method, of class Config.
     */
    @Test
    public void testGet() {
        out.println("get");

        assertNull(config.get("hello"));
        assertNotNull(config.get("fullscreen"));
    }

    /**
     * Test of getBoolean method, of class Config.
     */
    @Test
    public void testGetBoolean() {
        out.println("get boolean");
        assertFalse(config.getBoolean("helloworld"));
    }

    /**
     * Test of getInteger method, of class Config.
     */
    @Test
    public void testGetInteger() {
        out.println("get integer");

        config.getInteger("maxfps");

        try {
            config.getInteger("wrongkey");
            fail("unavailable integer");
        } catch (NumberFormatException e) {
        }
    }

    /**
     * Test of set method, of class Config.
     */
    @Test
    public void testSet_String_int() {
        out.println("set integer");
        String key = "testKey";
        int value = 121;

        config.set(key, value);

        assertNotNull(config.get(key));
        assertEquals(config.getInteger(key), value);
        assertEquals(config.get(key), String.valueOf(value));
    }

    /**
     * Test of set method, of class Config.
     */
    @Test
    public void testSet_String_boolean() {
        out.println("set boolean");
        String key = "testBooleanKey";
        boolean value = false;

        config.set(key, value);

        assertNotNull(config.get(key));
        assertEquals(config.get(key), String.valueOf(value));
        assertEquals(config.getBoolean(key), value);
    }

    /**
     * Test of set method, of class Config.
     */
    @Test
    public void testSet_String_String() {
        out.println("set string");
        String key = "testStringKey";
        String value = "testStringValue";

        config.set(key, value);

        assertNotNull(config.get(key));
        assertEquals(config.get(key), String.valueOf(value));
    }

    /**
     * Test of getInstance method, of class Config.
     */
    @Test
    public void testGetInstance() {
        out.println("getInstance");
        assertSame(config, Config.getInstance());
    }

    /**
     * Test of iterator method, of class Config.
     */
    @Test
    public void testIterator() {
        out.println("iterator");

        for (Object obj : config) {
            assertTrue(obj instanceof Map.Entry);
        }
    }

}