import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * JUnit test class for interface {@see Meeting}. It assumes an implementation called MeetingImpl.
 *
 * It does not test for the uniqueness of the ID, as this should be provided by the class in charge of issuing them,
 * or by the one above that. The Meeting class accepts the ID provided at construction time as final, without any
 * kind of checking.
 *
 * @author federico.bartolomei (BBK-PiJ-2014-21)  
 */
public class TestMeeting {
    Set<Contact> contacts;
    Calendar date;
    
    @Before
    public void setUp() {
        contacts = null;
        date = null;
    }
    
    @After
    public void tearDown() {
        contacts = null;
        date = new GregorianCalendar(2015,10,21,10,15);
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    public Contact createContactsMock(String name, int id, String notes) {
        Contact dummy = mock(Contact.class);
        when(dummy.getName()).thenReturn(name);
        when(dummy.getId()).thenReturn(id);
        when(dummy.getNotes()).thenReturn(notes);
        return dummy;
    }
    
    public Set<Contact> createSetOfContactsMock(int n) {
        Set<Contact> toReturn = new HashSet<Contact>();
        while(n>0) {
            toReturn.add(createContactsMock("Contact" + n, n, "Notes about Contact" + n));
        }
        return toReturn;
    }
    
    @Test
    public void createMeetingWithNullDateShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        Meeting nullDate = new MeetingImpl(null, contacts, 3);
    }
    
    @Test
    public void createMeetingWithNullContactsShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        Meeting nullContacts = new MeetingImpl(date, null, 10);
    }
    
    @Test
    public void createMeetingWithOneContactTestGetContactsSize() {
        contacts = createSetOfContactsMock(1);
        Meeting test = new MeetingImpl(date, contacts, 3);
        assertEquals(test.getContacts().size(), 1);
    }
    
    @Test
    public void createMeetingWith10ContactsTestGetContactsSize() {
        contacts = createSetOfContactsMock(10);
        Meeting test = new MeetingImpl(date, contacts, 4);
        assertEquals(test.getContacts().size(), 10);
    }
    
    @Test
    public void createMeetingShouldIgnoreDuplicateContacts() {
        // TODO
    }
    
    @Test
    public void testGetId() {
        // TODO
    }
    
    @Test
    public void testGetDate() {
        // TODO
    }
    
    @Test
    public void testGetContacts() {
        // TODO
    }
    
    

}
