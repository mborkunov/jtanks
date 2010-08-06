/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.system;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.image.VolatileImage;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.System.out;

public class ResourceManagerTest {

    public ResourceManagerTest() {
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
     * Test of preload method, of class ResourceManager.
     */
    @Test
    public void testPreload() {
        ResourceManager.preload();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVolatileImage method, of class ResourceManager.
     */
    @Test
    public void testGetVolatileImage() {
        //VolatileImage result = ResourceManager.getVolatileImage(name);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getImage method, of class ResourceManager.
     */
    @Test
    public void testGetImage_String() {
        //Image result = ResourceManager.getImage(name);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getImage method, of class ResourceManager.
     */
    @Test
    public void testGetImage_URL() {
        //Image result = ResourceManager.getImage(resource);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRandomQuote method, of class ResourceManager.
     */
    @Test
    public void testGetRandomQuote() {
        Quote expResult = null;
        Quote result = ResourceManager.getRandomQuote();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fileExists method, of class ResourceManager.
     */
    @Test
    public void testFileExists() {
        String filename = "";
        boolean expResult = false;
        boolean result = ResourceManager.fileExists(filename);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getImageResource method, of class ResourceManager.
     */
    @Test
    public void testGetImageResource() {
        String name = "";
        URL expResult = null;
        URL result = ResourceManager.getImageResource(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStream method, of class ResourceManager.
     */
    @Test
    public void testGetStream() {
        String name = "";
        InputStream expResult = null;
        InputStream result = ResourceManager.getStream(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAnimationResources method, of class ResourceManager.
     */
    @Test
    public void testGetAnimationResources() {
        String source = "";
        List<URL> expResult = null;
        List<URL> result = ResourceManager.getAnimationResources(source);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createInvisibleCursor method, of class ResourceManager.
     */
    @Test
    public void testCreateInvisibleCursor() {
        Cursor expResult = null;
        Cursor result = ResourceManager.createInvisibleCursor();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getImages method, of class ResourceManager.
     */
    @Test
    public void testGetImages() {
        //List<Image> result = ResourceManager.getImages(resources);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}