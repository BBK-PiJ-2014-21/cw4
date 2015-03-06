import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test class for interface {@see PastMeeting}. It assumes an implementation called PastMeetingImpl.
 * Null arguments in the constructor, in addNotes(String) and getNotes(String) are not tested as these
 * conditions should throw an exception from the caller class ContactManager.
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

    @Test
    public void testGetNotesEmptyString() {
        PastMeeting empty = new PastMeetingImpl(date, contacts, 10);
        assertEquals(empty.getNotes(), "");
    }

    @Test
    public void testGetNotes() {
        PastMeeting test = new PastMeetingImpl(date, contacts, 10, "Notes about a past meeting");
        assertEquals(test.getNotes(), "Notes about a past meeting");
    }

    @Test
    public void testAddNotesGetNotes() {
        PastMeeting test = new PastMeetingImpl(date, contacts, 10);
        test.addNotes("Some notes");
        assertEquals(test.getNotes(), "Some notes");
    }



}