import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test class for interface {@see Contact}. It assumes an implementation named ContactImpl.
 *
 * It does not test for the uniqueness of the ID, as this should be provided by the class in charge of issuing them,
 * or by the one above that. The Contact class accepts the ID provided at construction time as final, without any
 * kind of checking. 
 * 
 * @author federico.bartolomei (BBK-PiJ-2014-21)
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

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
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
    
    @Test
    public void testNullNameShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        Contact nullName = new ContactImpl(null, 12345, "Notes");
    }
    
    @Test
    public void testNullNotesShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        Contact nullNotes = new ContactImpl("John", 9000, null);
    }
    
    @Test
    public void testAddNullNotesShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        foo.addNotes(null);
    }
    
    
}
