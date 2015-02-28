package test;

import main.Contact;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for interface {@see Contact}. It assumes an implementation named ContactImpl.
 */
public class TestContact {
    Contact foo;
    Contact foo2;
    
    @Before
    public void setUp() {
        foo = new ContactImpl("Foo", 012345);
        foo2 = new ContactImpl("Foo2", 012345, "Some notes about Foo2.");
    }
    
    @After
    public void tearDown() {
        foo = null;
        foo2 = null;
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
    
    @Test
    public void testAddNotesInTwoTimes() {
        foo2.addNotes("More notes.");
        foo2.addNotes("Some more notes.");
        assertEquals(foo2.getNotes(), "Some notes about Foo2. More notes. Some more notes.");
    }
    
    
    
}
