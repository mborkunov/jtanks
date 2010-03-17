package jtanks.system;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuoteTest {

    public QuoteTest() {
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
     * Test of setAuthor and getAuthor methods, of class Quote.
     */
    @Test
    public void testSetGetAuthor() {
        System.out.println("set get author");

        Quote instance = new Quote();
        assertEquals(null, instance.getAuthor());

        String expResult = "Albert Einstein";
        instance.setAuthor("Another author");
        instance.setAuthor(expResult);

        assertEquals(expResult, instance.getAuthor());
        assertEquals("author", new Quote("author", null).getAuthor());
    }

    /**
     * Test of setContent and getContent methods, of class Quote.
     */
    @Test
    public void testSetGetContent() {
        System.out.println("set get content");
        String content = "Some text is here";
        Quote instance = new Quote();

        assertEquals(null, instance.getContent());
        instance.setContent(content);
        assertEquals(content, instance.getContent());

        assertEquals("content", new Quote(null, "content").getContent());
    }

    /**
     * Test of toString method, of class Quote.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Quote instance = new Quote("Author", "Quote content");

        assertEquals("Author: \"Quote content\"", instance.toString());
        
        
        assertEquals("null: \"null\"", new Quote().toString());


    }
}