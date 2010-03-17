package jtanks.system;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class RegistryTest {

    public RegistryTest() {
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
     * Test of set get methods, of class Registry.
     */
    @Test
    public void testSetGet() {
        System.out.println("set get");
        Object key = Object.class.getClass();
        Object value = key.toString();

        assertNull(Registry.get(key));

        boolean exception = false;
        try {
            Registry.set("hello", null);
        } catch (IllegalArgumentException e) {
            exception = true;
        } finally {
            if (exception == false) {
                fail("Value cannot be null");
            }
        }

        Registry.set(key, value);

        assertEquals(value, Registry.get(key));
    }

    @Test
    public void testRemove() {
        System.out.println("remove");
        Registry.set("hello", "bye");
        assertNotNull(Registry.get("hello"));
        Registry.remove("hello");
        assertNull(Registry.get("hello"));
    }

}