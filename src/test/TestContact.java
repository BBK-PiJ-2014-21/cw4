package test;

import main.Contact;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for interface {@see Contact}. It assumes an implementation named ContactImpl.
 */
public class TestContact {
    Contact foo;
    
    @Before
    public void setUp() {
        foo = new ContactImpl("Foo", 012345);
    }
    
    @After
    public void tearDown() {
        foo = null;
    }
    
    @Test
    public void testGetID() {
        assertEquals(foo.getId(), 012345);
    }
    
    @Test
    public void testGetName() {
        assertEquals(foo.getName(), "Foo");
    }
    
    @Test
    public void testEmptyNotes() {
        assertEquals(foo.getNotes(), "");
    }
    
    @Test
    public void testAddNotesGetNotes() {
        foo.addNotes("Some notes about Foo.");
        assertEquals(foo.getNotes(), "Some notes about Foo.");
    }
    
    
    
    
    
    
}
