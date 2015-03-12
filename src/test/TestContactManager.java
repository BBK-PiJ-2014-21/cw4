import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.junit.Assert.*;

/**
 * JUnit test class for interface {@see ContactManager}. It assumes an implementation called ContactManagerImpl.
 *
 * @author federico.bartolomei (BBK-PiJ-2014-21)
 */
public class TestContactManager {
    private ContactManager test;

    @Before
    public void setUp() {
        test = new ContactManagerImpl();
    }

    @After
    public void tearDown() {
        test = null;
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    // testing addNewContact() and getContacts()   -----------------------------------------------------

    /**
     * This method is used in following tests to create n valid contacts and add them to
     * the ContactManager list
     *
     * @param n the number of contacts to be added to the list.
     */
    public void addContacts(int n) {
        for (int i = 1; i <=n; i++) {
            test.addNewContact("Contact" + i, "Notes about Contact" + i);
        }
    }

    /**
     * This method is used in following tests to create n valid contacts with same name
     * and add them to the ContactManager list
     *
     * @param n the number of contacts to be added to the list.
     */
    public void addContactsSmith(int n) {
        for (int i = 1; i<=n; i++) {
            test.addNewContact("Smith", "Notes about Smith" + "(" + i + ")");
        }
    }

    @Test
    public void testAddNewContactWithNullNameShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        test.addNewContact(null, "Some notes");
    }

    @Test
    public void testAddNewContactWithNullNotesShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        test.addNewContact("Contact1", null);
    }

    @Test
    public void testAddNewContactGetContactByName() {
        test.addNewContact("Contact1", "Notes about Contact1");
        Contact first = (Contact)test.getContacts("Contact1").toArray()[0];
        assertEquals(first.getName(), "Contact1");
    }

    @Test
    public void testAddNewContactsShouldGetTheSameOneBothByNameAndId() {
        addContacts(10);
        Set<Contact> byNameshouldBeSize1 = test.getContacts("Contact1");
        assertEquals(byNameshouldBeSize1.size(), 2);
        Contact first = (Contact)byNameshouldBeSize1.toArray()[0];
        Set<Contact> byIDshouldBeSize1 = test.getContacts(first.getId());
        Contact firstcopy = (Contact)byIDshouldBeSize1.toArray()[0];
        assertEquals(first, firstcopy);
    }

    @Test
    public void testAddTwoNewContactsShouldIssueTwoDifferentIds() {
        test.addNewContact("Contact1", "Notes about Contact1");
        test.addNewContact("Contact2", "Notes about Contact2");
        int id1 = ((Contact)test.getContacts("Contact1").toArray()[0]).getId();
        int id2 = ((Contact)test.getContacts("Contact2").toArray()[0]).getId();
        assertNotEquals(id1, id2);
    }

    @Test
    public void testAdd10ContactsWhoseNameContainsSameStringGetByName() {
        addContacts(10);
        Set<Contact> list = test.getContacts("Contact");
        assertEquals(list.size(), 10);
        for(Contact contact : list) {
            assertTrue(contact.getName().contains("Contact"));
        }
    }

    @Test
    public void testAdd10ContactsWhoseNameIsSameStringPlusOtherThatDoesntGetByName() {
        addContacts(5);
        addContactsSmith(10);
        Set<Contact> list = test.getContacts("Smith");
        assertEquals(list.size(), 10);
        for(Contact contact : list) {
            assertTrue(contact.getName().equals("Smith"));
        }
    }

    @Test
    public void testAddMoreNewContactsWhoseNameContainsSameStringGetByNameCheckAllIDsAreUnique() {
        addContacts(10);
        addContacts(10);
        Set<Contact> list = test.getContacts("Contact");
        assertEquals(list.size(), 20);
        Contact[] contacts = test.getContacts("Contact").toArray(new Contact[list.size()]);
        for(int i=0; i<contacts.length; i++) {
            for(int j=i+1; j<contacts.length; j++) {
                assertNotEquals(contacts[i].getId(), contacts[j].getId());
            }
        }
    }

    @Test
    public void testAddMoreContactsWithSomeDifferentSomeSameNameMergeSetsAndCheckAllIDsAreUnique() {
        addContacts(5);
        addContactsSmith(5);
        Set<Contact> different = test.getContacts("Contact");
        Set<Contact> same = test.getContacts("Smith");
        Set<Contact> merged = new HashSet<Contact>();
        merged.addAll(same);
        merged.addAll(different);
        Contact[] mergedArray = merged.toArray(new Contact[merged.size()]);
        for (int i = 0; i < mergedArray.length; i++) {
            for (int j = 0; i < mergedArray.length; i++) {
                if (i == j) {
                    continue;
                }
                assertTrue(mergedArray[i] != mergedArray[j]);
            }
        }
    }

    @Test
    public void testAddMoreNewContactsWhoseNameIsSameStringGetByNameMergeSetsAndCheckAllIDsAreUnique() {
        addContactsSmith(10);
        Set<Contact> list1 = test.getContacts("Smith");
        addContactsSmith(5);
        Set<Contact> list2 = test.getContacts("Smith");
        Set<Contact> merged = new HashSet<Contact>();
        merged.addAll(list1);
        merged.addAll(list2);
        Contact[] arrayList = merged.toArray(new Contact[merged.size()]);
        for(int i=0; i<arrayList.length; i++) {
            for(int j=0; j<arrayList.length; j++) {
                if(i==j) {
                    continue;
                }
                assertNotEquals(arrayList[i].getId(), arrayList[j].getId());
            }
        }
    }

    @Test
    public void testAddMoreNewContactsGetAllContactsByNameAndByArrayOfIDsCompareSetsSize() {
        addContacts(5);
        Set<Contact> listByName = test.getContacts("Contact");
        int[] IDs = new int[listByName.size()];
        int i = 0;
        for(Contact c : listByName) {
            IDs[i] = c.getId();
            i++;
        }
        Set<Contact> listByIDs = test.getContacts(IDs);
        assertEquals(listByIDs.size(), listByName.size());
    }

    @Test
    public void testAddOneNewContactGetContactByIdOneValidOneNotCorresponding() {
        exception.expect(IllegalArgumentException.class);
        addContacts(1);
        Set<Contact> list = test.getContacts("Contact1");
        assertEquals(list.size(), 1);
        Contact first = (Contact)list.toArray()[0];
        int validID = first.getId();
        test.getContacts(validID, 1231);
    }

    @Test
    public void testGetContactEmptyStringShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        test.getContacts("");
    }

    @Test
    public void testGetContactByIdOnEmptyListShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        test.getContacts(100);
    }

    @Test
    public void testGetContactNullShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        String nullString = null;
        test.getContacts(nullString);
    }

    @Test
    public void testGetContactNameNotInTheListShouldReturnAnEmptySet() {
        Set<Contact> empty = test.getContacts("Name not in the list");
        assertEquals(empty.size(), 0);
    }

    // testing addFutureMeeting(), getFutureMeeting(), getMeeting() ------------------------------------

    /**
     * This method is used in some following tests to create a Set of n contacts none of which
     * is added to the ContactManager list.
     *
     * @param n number of contacts to add to the Set.
     * @return the Set of invalid (unknown) contacts.
     */
    public Set<Contact> createUnknownSetOfContacts(int n) {
        Set<Contact> contacts = new HashSet<Contact>();
        while(n>0) {
            Contact contact = new ContactImpl("Contact" + n, n);
            contacts.add(contact);
            n--;
        }
        return contacts;
    }

    /**
     * This method is used in following tests to quickly get a Calendar instance of a future date
     *
     * @return a future date.
     */
    public Calendar getFutureDate() {
        Calendar date = new GregorianCalendar(3015, 12, 12, 12, 12);
        return date;
    }

    /**
     * This method is used in following tests to quickly get a Calendar instance of a past date
     *
     * @return a past date.
     */
    public Calendar getPastDate() {
        Calendar date = new GregorianCalendar(2001, 12, 12, 12, 12);
        return date;
    }

    @Test
    public void addFutureMeetingWithNullContactsShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        test.addFutureMeeting(null, getFutureDate());
    }

    @Test
    public void addFutureMeetingWithNullDateShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        test.addNewContact("Valid", "Notes");
        test.addFutureMeeting(test.getContacts("Valid"), null);
    }

    @Test
    public void addFutureMeetingWithUnknownContactsShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        test.addFutureMeeting(createUnknownSetOfContacts(1), getFutureDate());
    }

    @Test
    public void addFutureMeetingWithSomeKnownSomeUnknownContactsShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        Set<Contact> mixed = new HashSet<Contact>();
        test.addNewContact("Valid", "Notes");
        Set<Contact> valid = test.getContacts("Valid");
        Contact validContact = valid.toArray(new Contact[valid.size()])[0];
        Contact invalidContact = new ContactImpl("Not Valid", 100, "Notes");
        mixed.add(validContact);
        mixed.add(invalidContact);
        test.addFutureMeeting(mixed, getFutureDate());
    }

    @Test
    public void addFutureMeetingWithPastDateShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        test.addNewContact("Valid", "Notes");
        test.addFutureMeeting(test.getContacts("Valid"), getPastDate());
    }

    @Test
    public void addFutureMeetingGetFutureMeetingCheckIfTheIdCorresponds() {
        test.addNewContact("Valid", "Notes");
        int thisID = test.addFutureMeeting(test.getContacts("Valid"), getFutureDate());
        FutureMeeting thisMeeting = test.getFutureMeeting(thisID);
        assertEquals(thisID, thisMeeting.getId());
    }

    @Test
    public void addFutureMeetingGetMeetingCheckIfTheIdCorresponds() {
        test.addNewContact("Valid", "Notes");
        int thisID = test.addFutureMeeting(test.getContacts("Valid"), getFutureDate());
        Meeting thisMeeting = test.getMeeting(thisID);
        assertEquals(thisID, thisMeeting.getId());
    }

    @Test
    public void addFutureMeetingGetMeetingCompareContacts() {
        test.addNewContact("Valid", "Notes");
        Set<Contact> set1 = test.getContacts("Valid");
        Contact contact1 = set1.toArray(new Contact[set1.size()])[0];
        int thisID = test.addFutureMeeting(set1, getFutureDate());
        Meeting thisMeeting = test.getMeeting(thisID);
        Set<Contact> set2 = thisMeeting.getContacts();
        Contact contact2 = set2.toArray(new Contact[set2.size()])[0];
        assertEquals(contact1, contact2);
    }

    @Test
    public void addFutureMeetingGetFutureMeetingCompareSetsOfContacts() {
        test.addNewContact("Valid1", "Notes1");
        test.addNewContact("Valid2", "Notes2");
        Set<Contact> set1 = test.getContacts("Valid");
        int thisID = test.addFutureMeeting(set1, getFutureDate());
        FutureMeeting thisMeeting = test.getFutureMeeting(thisID);
        Set<Contact> set2 = thisMeeting.getContacts();
        Contact[] first = set1.toArray(new Contact[set1.size()]);
        Contact[] second = set2.toArray(new Contact[set2.size()]);
        assertThat(first[0], Matchers.either(Matchers.is(second[0])).or(Matchers.is(second[1])));
        assertThat(first[1], Matchers.either(Matchers.is(second[0])).or(Matchers.is(second[1])));
    }

    @Test
    public void getMeetingNonExistentIdShouldReturnNull() {
        assertNull(test.getMeeting(100));
    }

    @Test
    public void getFutureMeetingWithNonExistentIdShouldReturnNull() {
        assertNull(test.getFutureMeeting(100));
    }

    @Test
    public void addFutureMeetingWithMatchingContactNotRetrievedFromGetContactsShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        test.addNewContact("Valid", "Notes");
        Set<Contact> validSet = test.getContacts("Valid");
        Contact validContact = validSet.toArray(new Contact[validSet.size()])[0];
        int validID = validContact.getId();
        Set<Contact> invalidSet = new HashSet<Contact>();
        Contact invalidContact = new ContactImpl("Valid", validID, "Notes");
        invalidSet.add(invalidContact);
        test.addFutureMeeting(invalidSet, getFutureDate());
    }

    // testing getFutureMeetingList(), getPastMeetingList()   ------------------------------------------

    /**
     * This method is used in various following tests to avoid code repetition.
     * It adds a new contact with name "Valid" to the test ContactManager list of contacts,
     * and returns it via the Set<Contacts> of method getContacts().
     *
     * @return the contact added to the ContactManger.
     */
    public Contact addContactgetContact() {
        test.addNewContact("Valid", "Notes");
        Set<Contact> validSet = test.getContacts("Valid");
        return validSet.toArray(new Contact[validSet.size()])[0];
    }

    @Test
    public void getFutureMeetingListWithNullContactsShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        Contact nullContact = null;
        test.getFutureMeetingList(nullContact);
    }

    @Test
    public void getFutureMeetingListWithNonExistentContactShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        Contact notRegistered = new ContactImpl("Not valid", 10);
        test.getFutureMeetingList(notRegistered);
    }

    @Test
    public void getFutureMeetingListWithValidContactButNoMeetingScheduledShouldReturnAnEmptyList() {
        Contact valid = addContactgetContact();
        assertTrue(test.getFutureMeetingList(valid).isEmpty());
    }

    @Test
    public void getFutureMeetingListWithValidContactOneMeetingScheduledShouldReturnIt() {
        Contact valid = addContactgetContact();
        int meetingId = test.addFutureMeeting(test.getContacts("Valid"), getFutureDate());
        assertEquals(test.getFutureMeetingList(valid).size(), 1);
        assertEquals(test.getFutureMeetingList(valid).get(0).getId(), meetingId);
    }

    @Test
    public void getFutureMeetingListWithValidContactFewMeetingScheduledShouldReturnThemSorted() {
        Contact valid = addContactgetContact();
        int third = test.addFutureMeeting(test.getContacts("Valid"), getFutureDate());  // it is year 3015
        int first = test.addFutureMeeting(test.getContacts("Valid"), new GregorianCalendar(2018, 6, 4, 16, 30));
        int second = test.addFutureMeeting(test.getContacts("Valid"), new GregorianCalendar(2020, 12, 12, 13, 13));
        List<Meeting> list = test.getFutureMeetingList(valid);
        assertEquals(list.size(), 3);
        assertEquals(list.get(0).getId(), first);
        assertEquals(list.get(1).getId(), second);
        assertEquals(list.get(2).getId(), third);
    }

    @Test
    public void getFutureMeetingListWithValidContactFewMeetingsShouldReturnAListWithNoDuplicates() {
     // TODO (should implement control over same contacts and date meetings? Probably not.)
    }

    @Test
    public void getFutureMeetingListWithNullDateShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        Calendar nullDate = null;
        test.getFutureMeetingList(nullDate);
    }

    @Test
    public void getFutureMeetingListWithPastDateNoMeetingsMatchingShouldReturnAnEmptyList() {
        assertTrue(test.getFutureMeetingList(getPastDate()).isEmpty());
    }

    @Test
    public void getFutureMeetingListWithFutureDateNoMeetingsMatchingShouldReturnAnEmptyList() {
        assertTrue(test.getFutureMeetingList(getFutureDate()).isEmpty());
    }

    @Test
    public void getFutureMeetingListWithPastDateOneMeetingMatchingShouldReturnIt() {
       // TODO (need addNewPastMeeting to be tested)
    }

    @Test
    public void getFutureMeetingListWithPastDateFewMeetingsMatchingShouldReturnThemSorted() {
        // TODO (need addNewPastMeeting to be tested)
    }

    @Test
    public void getFutureMeetingListWithFutureDateOneMeetingsMatchingShouldReturnIt() {
        addContactgetContact();
        int id = test.addFutureMeeting(test.getContacts("Valid"), getFutureDate());
        List<Meeting> list = test.getFutureMeetingList(getFutureDate());
        assertEquals(list.get(0).getId(), id);
    }

    @Test
    public void getFutureMeetingListWithFutureDateFewMeetingsMatchingShouldReturnThemSortedByTime() {
        // three same dates, different times to be added to three meetings
        Calendar date1 = new GregorianCalendar(2018, 10, 10, 10, 10);
        Calendar date2 = new GregorianCalendar(2018, 10, 10, 10, 30);
        Calendar date3 = new GregorianCalendar(2018, 10, 10, 20, 20);
        // the date to use as parameter of getting the list of future meetings
        Calendar param = Calendar.getInstance();
        param.set(2018, 10, 10);
        // add new three meetings via different ways (one through id, few with same name, few with different names)
        Contact valid = addContactgetContact();
        int validID = valid.getId();
        addContactsSmith(10);
        addContacts(3);
        int second = test.addFutureMeeting(test.getContacts("Valid"), date2);
        int third = test.addFutureMeeting(test.getContacts("Cont"), date3);
        int first = test.addFutureMeeting(test.getContacts(validID), date1);
        List<Meeting> meetings = test.getFutureMeetingList(param);
        assertEquals(meetings.size(), 3);
        assertEquals(meetings.get(0).getId(), first);
        assertEquals(meetings.get(1).getId(), second);
        assertEquals(meetings.get(2).getId(), third);
    }

    @Test
    public void getFutureMeetingListWithFutureDateFewMeetingsMatchingShouldHaveNoDuplicates() {
        // TODO (should implement control over same contacts and date meetings? Probably not.)
    }

    @Test
    public void getFutureMeetingListWithPastDateFewMeetingsMatchingShouldHaveNoDuplicates() {
        // TODO (should implement control over same contacts and date meetings? Probably not.)
    }

    @Test
    public void getPastMeetingListWithNullContactShouldThrowNullPointerException() {
        // TODO
    }

    @Test
    public void getPastMeetingListWithNonExistentContactShouldThrowIllegalArgumentException() {
        // TODO
    }

    @Test
    public void getPastMeetingWithValidContactNoMeetingsShouldReturnAnEmptyList() {
        // TODO
    }

    @Test
    public void getPastMeetingWithValidContactOneMeetingShouldReturnIt() {
        // TODO
    }

    @Test
    public void getPastMeetingWithValidContactFewMeetingsShouldReturnThemSorted() {
        // TODO
    }



    // TODO testing addNewPastMeeting(), getPastMeeting()   -------------------------------------------------

    @Test
    public void getFutureMeetingWithPastMeetingIdShouldThrowIllegalArgumentException() {
    }

    @Test
    public void convertFutureMeetingToPastMeetingCompareWithMeeting() {
    }

    @Test
    public void addNewPastMeetingWithNullContactsShouldThrowNullPointerException() {

    }

    @Test
    public void addNewPastMeetingWithNullDateShouldThrowNullPointerException() {

    }

    @Test
    public void addNewPastMeetingWithNullTextShouldThrowNullPointerException() {

    }

    @Test
    public void addNewPastMeetingWithEmptyContactSetShouldThrowIllegalArgumentException() {

    }

    @Test
    public void addNewPastMeetingWithNonExistentContactsShouldThrowIllegalArgumentException() {

    }

    @Test
    public void addNewPastMeetingWithASetOfSomeExistentSomeNonExistentContactsShouldThrowIllegalArgumentException() {

    }

    @Test
    public void getPastMeetingWithIdOfAFutureMeetingShouldThrowIllegalArgumentException() {

    }

    @Test
    public void getPastMeetingWithNonExistentIdShouldReturnNull() {

    }


}
