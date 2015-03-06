import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * JUnit test class for interface {@see PastMeeting}. It assumes an implementation called PastMeetingImpl.
 *
 * @author federico.bartolomei (BBK-PiJ-2014-21)
 */
public class TestPastMeeting {
    Set<Contact> contacts;
    Calendar date;

    @Before
    public void setUp() {
        contacts = new HashSet();
        date = new GregorianCalendar(2015,10,21,10,15);
    }

    @After
    public void tearDown() {
        contacts = null;
        date = null;
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public Contact createContactMock() {
        Contact dummy = mock(Contact.class);
        when(dummy.getName()).thenReturn("Contact1");
        when(dummy.getId()).thenReturn(1000);
        when(dummy.getNotes()).thenReturn("Some notes");
        return dummy;
    }

    @Test
    public void testNullDateShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        PastMeeting nullDate = new PastMeetingImpl(null, contacts, 10, "Stringy");
    }

    @Test
    public void testNullContactsShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        PastMeeting nullContacts = new PastMeetingImpl(date, null, 10);
    }

    @Test
    public void testEmptyContactSetShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        PastMeeting empty = new PastMeetingImpl(date, contacts, 10);
    }

    @Test
    public void testNullNotesShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        contacts.add(createContactMock());
        PastMeeting nullNotes = new PastMeetingImpl(date, contacts, 10, null);
    }

    @Test
    public void testGetNotesEmptyString() {
        contacts.add(createContactMock());
        PastMeeting empty = new PastMeetingImpl(date, contacts, 10);
        assertEquals(empty.getNotes(), "");
    }

    @Test
    public void testGetNotes() {
        contacts.add(createContactMock());
        PastMeeting test = new PastMeetingImpl(date, contacts, 10, "Notes about a past meeting");
        assertEquals(test.getNotes(), "Notes about a past meeting");
    }

    @Test
    public void testAddNotesGetNotes() {
        contacts.add(createContactMock());
        PastMeetingImpl test = new PastMeetingImpl(date, contacts, 10);
        test.addNotes("Some notes");
        assertEquals(test.getNotes(), "Some notes");
    }



}