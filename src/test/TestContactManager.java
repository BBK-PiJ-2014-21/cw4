import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;

/**
 * JUnit test class for interface {@see ContactManager}. It assumes an implementation called ContactManagerImpl.
 *
 * @author federico.bartolomei (BBK-PiJ-2014-21)
 */
public class TestContactManager {
    private ContactManager test;
    private File file;

    @Before
    public void setUp() {
        test = new ContactManagerImpl();
        file = new File("Contact.txt");
    }

    @After
    public void tearDown() {
        test = null;
        file.delete();
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * This method is used in following tests to create n valid contacts with different names
     * and add them to the ContactManager list
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

    // testing addNewContact() and getContacts()   --------------------------------------------------------------------

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
        Set<Contact> byNameShouldBeSize1 = test.getContacts("Contact1");
        assertEquals(byNameShouldBeSize1.size(), 2);
        Contact first = (Contact)byNameShouldBeSize1.toArray()[0];
        Set<Contact> byIdShouldBeSize1 = test.getContacts(first.getId());
        Contact firstcopy = (Contact)byIdShouldBeSize1.toArray()[0];
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
    public void testAddOneNewContactGetContactByIdOneNotCorrespondingOneValid() {
        exception.expect(IllegalArgumentException.class);
        addContacts(1);
        Set<Contact> list = test.getContacts("Contact1");
        assertEquals(list.size(), 1);
        Contact first = (Contact)list.toArray()[0];
        int validID = first.getId();
        test.getContacts(1231, validID);
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

    // testing addFutureMeeting(), getFutureMeeting(), getMeeting() ---------------------------------------------------

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

    @Test
    public void getFutureMeetingWithPastMeetingIdShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        addContacts(3);
        test.addNewPastMeeting(test.getContacts("C"), getPastDate(), "Notes");
        int pastId = test.getFutureMeetingList(getPastDate()).get(0).getId();
        test.getFutureMeeting(pastId);
    }

    // testing getFutureMeetingList(), getPastMeetingList()   ---------------------------------------------------------

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
        Contact valid = addContactgetContact();
        test.addFutureMeeting(test.getContacts("Valid"), getFutureDate());
        test.addFutureMeeting(test.getContacts("Valid"), new GregorianCalendar(2018, 6, 4, 16, 30));
        test.addFutureMeeting(test.getContacts("Valid"), new GregorianCalendar(2020, 12, 12, 13, 13));
        List<Meeting> list = test.getFutureMeetingList(valid);
        Meeting[] array = list.toArray(new Meeting[list.size()]);
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array.length; j++) {
                if(i!=j) {
                    assertNotEquals(array[i], array[j]);
                }
            }
        }
    }

    @Test
    public void getFutureMeetingListWithPastDateFewMeetingsMatchingShouldHaveNoDuplicates() {
        Contact valid = addContactgetContact();
        Calendar date1 = getFutureDate();
        Calendar date2 = getFutureDate();
        Calendar date3 = getFutureDate();
        test.addFutureMeeting(test.getContacts("Valid"), date1);
        test.addFutureMeeting(test.getContacts("Valid"), date2);
        test.addFutureMeeting(test.getContacts("Valid"), date3);
        date1.set(1985, 21, 9);
        date2.set(1983, 02, 10);
        date3.set(1990, 10, 10);
        List<Meeting> list = test.getFutureMeetingList(valid);
        Meeting[] array = list.toArray(new Meeting[list.size()]);
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array.length; j++) {
                if(i!=j) {
                    assertNotEquals(array[i], array[j]);
                }
            }
        }
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
        Calendar changingDate = Calendar.getInstance();
        changingDate.set(2020, Calendar.OCTOBER, 10);
        addContactsSmith(3);
        int id = test.addFutureMeeting(test.getContacts("Smith"),changingDate);
        changingDate.set(2001, Calendar.OCTOBER, 10);
        List<Meeting> list = test.getFutureMeetingList(changingDate);
        assertEquals(test.getMeeting(id), list.get(0));
    }

    @Test
    public void getFutureMeetingListWithPastDateFewMeetingsMatchingShouldReturnThemSortedByTime() {
        // three same dates, different times to be added to three meetings (future date to avoid exception)
        Calendar date1 = new GregorianCalendar(2020, 10, 10, 10, 10);
        Calendar date2 = new GregorianCalendar(2020, 10, 10, 10, 30);
        Calendar date3 = new GregorianCalendar(2020, 10, 10, 20, 20);
        Calendar shouldBeIgnored = new GregorianCalendar(2019, 10, 10, 10, 20);
        // the date to use as parameter of getting the list of future meetings
        Calendar param = Calendar.getInstance();
        param.set(2010, 10, 10);
        // add new three meetings via different ways (one through id, few with same name, few with different names)
        Contact valid = addContactgetContact();
        int validID = valid.getId();
        addContactsSmith(10);
        addContacts(3);
        int ignored = test.addFutureMeeting(test.getContacts("Smith"), shouldBeIgnored);
        int second = test.addFutureMeeting(test.getContacts("Valid"), date2);
        int third = test.addFutureMeeting(test.getContacts("Cont"), date3);
        int first = test.addFutureMeeting(test.getContacts(validID), date1);
        // set the dates of the meetings to the past
        date1.set(2010, 10, 10, 10, 10);
        date2.set(2010, 10, 10, 10, 30);
        date3.set(2010, 10, 10, 20, 20);
        List<Meeting> meetings = test.getFutureMeetingList(param);
        assertEquals(meetings.size(), 3);
        assertEquals(meetings.get(0).getId(), first);
        assertEquals(meetings.get(1).getId(), second);
        assertEquals(meetings.get(2).getId(), third);
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
        Calendar shouldBeIgnored = new GregorianCalendar(2018, 10, 9, 20, 20);
        // the date to use as parameter of getting the list of future meetings
        Calendar param = Calendar.getInstance();
        param.set(2018, 10, 10);
        // add new three meetings via different ways (one through id, few with same name, few with different names)
        Contact valid = addContactgetContact();
        int validID = valid.getId();
        addContactsSmith(10);
        addContacts(3);
        int ignored = test.addFutureMeeting(test.getContacts("Smith"), shouldBeIgnored);
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
    public void getPastMeetingListWithNullContactShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        test.getPastMeetingList(null);
    }

    @Test
    public void getPastMeetingListWithNonExistentContactShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        Set<Contact> unknownSet = createUnknownSetOfContacts(1);
        Contact unknown = (Contact) unknownSet.toArray()[0];
        test.getPastMeetingList(unknown);
    }

    @Test
    public void getPastMeetingListWithValidContactNoMeetingsShouldReturnAnEmptyList() {
        Contact valid = addContactgetContact();
        assertTrue(test.getPastMeetingList(valid).isEmpty());
    }

    @Test
    public void getPastMeetingListWithValidContactOnePastMeetingShouldReturnIt() {
        Contact valid = addContactgetContact(); // add a contact with name "Valid"
        Set<Contact> set = test.getContacts("V");
        test.addNewPastMeeting(set, getPastDate(), "Notes");
        List<PastMeeting> list = test.getPastMeetingList(valid);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getContacts(), set);
    }

    @Test
    public void getPastMeetingListWithValidContactFewPastMeetingsShouldReturnThemSorted() {
        Contact valid = addContactgetContact(); // add a contact with name "Valid"
        addContacts(10);    // add new contacts with name "Contact", should be ignored
        test.addNewPastMeeting(test.getContacts("Contact"), getPastDate(), "Should be ignored");
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        Calendar date3 = Calendar.getInstance();
        date1.set(1985, Calendar.OCTOBER, 10);
        date2.set(1990, Calendar.JANUARY, 9);
        date3.set(1990, Calendar.JANUARY, 10);
        Set<Contact> set = test.getContacts("V");
        test.addNewPastMeeting(set, getPastDate(), "Notes about the 2001 Meeting");
        test.addNewPastMeeting(set, date3, "Notes about the 10/01/1990 Meeting");
        test.addNewPastMeeting(set, date2, "Notes about the 09/01/1990 Meeting");
        test.addNewPastMeeting(set, date1, "Notes about the 1985 Meeting");
        List<PastMeeting> list = test.getPastMeetingList(valid);
        PastMeeting[] array = list.toArray(new PastMeeting[list.size()]);
        assertEquals(list.size(), 4);
        assertEquals(array[0].getNotes(), "Notes about the 1985 Meeting");
        assertEquals(array[1].getNotes(), "Notes about the 09/01/1990 Meeting");
        assertEquals(array[2].getNotes(), "Notes about the 10/01/1990 Meeting");
        assertEquals(array[3].getNotes(), "Notes about the 2001 Meeting");
    }

    // testing addNewPastMeeting(), getPastMeeting()   ----------------------------------------------------------------

    @Test
    public void addNewPastMeetingWithNullContactsShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        test.addNewPastMeeting(null, getPastDate(), "Notes");
    }

    @Test
    public void addNewPastMeetingWithNullDateShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        addContacts(3);
        test.addNewPastMeeting(test.getContacts("Contact"), null, "Notes");
    }

    @Test
    public void addNewPastMeetingWithNullTextShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        addContactsSmith(1);
        test.addNewPastMeeting(test.getContacts("Smith"), getPastDate(), null);
    }

    @Test
    public void addNewPastMeetingWithEmptyContactSetShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        Set<Contact> empty = new HashSet<>();
        test.addNewPastMeeting(empty, getPastDate(), "Notes");
    }

    @Test
    public void addNewPastMeetingWithNonExistentContactsShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        Set<Contact> unknown = createUnknownSetOfContacts(10);
        test.addNewPastMeeting(unknown, getPastDate(), "Notes");
    }

    @Test
    public void addNewPastMeetingWithSomeExistentSomeNonExistentContactsShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        Contact unknown = new ContactImpl("Unknown", 10, "Notes");
        Contact known = addContactgetContact();
        Set<Contact> set = new HashSet<>();
        set.add(known);
        set.add(unknown);
        test.addNewPastMeeting(set, getPastDate(), "Notes");
    }

    @Test
    public void addNewPastMeetingWithFutureDateShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        addContacts(3);
        test.addNewPastMeeting(test.getContacts("C"), getFutureDate(), "Notes");
    }

    @Test
    public void addNewPastMeetingGetFutureMeetingListCompareWithMeetingDowncasted() {
        addContacts(1);
        test.addNewPastMeeting(test.getContacts("Contact"), getPastDate(), "Notes should match");
        List<Meeting> list = test.getFutureMeetingList(getPastDate());
        assertEquals(list.size(), 1);
        PastMeeting m = (PastMeeting)list.get(0);
        assertEquals("Notes should match", m.getNotes());
    }

    @Test
    public void addNewPastMeetingGetFutureMeetingListCompareGetMeetingWithGetPastMeetingIds() {
        addContacts(5);
        test.addNewPastMeeting(test.getContacts("C"), getPastDate(), "Notes");
        Meeting pastMeeting = test.getFutureMeetingList(getPastDate()).get(0);
        int id = pastMeeting.getId();
        assertEquals(test.getMeeting(id), test.getPastMeeting(id));
    }

    @Test
    public void addFewNewPastMeetingAndAddFewFutureMeetingAllOfThemShouldHaveDifferentIds() {
        addContactsSmith(10);
        addContacts(5);
        test.addNewPastMeeting(test.getContacts("Contact"), getPastDate(), "Notes");
        test.addNewPastMeeting(test.getContacts("Smith"), getPastDate(), "Notes");
        List<Meeting> pastMeetings = test.getFutureMeetingList(getPastDate());
        int id1 = pastMeetings.get(0).getId();
        int id2 = pastMeetings.get(1).getId();
        Set<Contact> set = new HashSet<>();
        set.add(addContactgetContact());
        int id3 = test.addFutureMeeting(set, getFutureDate());
        int id4 = test.addFutureMeeting(test.getContacts("Smith"), getFutureDate());
        int[] array = new int[] {id1, id2, id3, id4};
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array.length; j++) {
                if(i!=j) {
                    assertTrue(array[i]!=array[j]);
                }
            }
        }
    }

    @Test
    public void getPastMeetingWithIdOfAFutureMeetingShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        addContacts(1);
        int futureID = test.addFutureMeeting(test.getContacts("C"), getFutureDate());
        test.addNewPastMeeting(test.getContacts("C"), getPastDate(), "Notes");  // should be ignored
        test.getPastMeeting(futureID);
    }

    @Test
    public void getPastMeetingWithNonExistentIdShouldReturnNull() {
        assertNull(test.getPastMeeting(100));
    }

    @Test
    public void getPastMeetingFromAddNewPastMeetingShouldReturnIt() {
        addContacts(3);
        test.addNewPastMeeting(test.getContacts("Contact1"), getPastDate(), "Meeting 1");
        int id1 = test.getFutureMeetingList(getPastDate()).get(0).getId();
        Calendar date2 = Calendar.getInstance();
        date2.set(1968, 21, 6);
        Set<Contact> set2 = test.getContacts("Contact2");
        test.addNewPastMeeting(set2, date2, "Meeting 2");
        int id2 = test.getFutureMeetingList(date2).get(0).getId();
        PastMeeting p1 = test.getPastMeeting(id1);
        PastMeeting p2 = test.getPastMeeting(id2);
        assertEquals(p1.getNotes(), "Meeting 1");
        assertEquals(p2.getContacts(), set2);
    }

    // testing addMeetingNotes() --------------------------------------------------------------------------------------

    @Test
    public void addMeetingNotesWithValidIdNullNotesShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        addContacts(1);
        Calendar date = Calendar.getInstance();
        date.set(2020, Calendar.OCTOBER, 10);
        int id = test.addFutureMeeting(test.getContacts("C"),date);
        date.set(2010, Calendar.OCTOBER, 10);
        test.addMeetingNotes(id, null);
    }

    @Test
    public void addMeetingNotesWithNonExistentIdShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        test.addMeetingNotes(1010, "Notes");
    }

    @Test
    public void addMeetingNotesOnAMeetingSetUpInTheFutureShouldThrowIllegalStateException() {
        exception.expect(IllegalStateException.class);
        addContacts(1);
        int futureID = test.addFutureMeeting(test.getContacts("C"), getFutureDate());
        test.addMeetingNotes(futureID, "Notes");
    }

    @Test
    public void addMeetingNotesCheckMeetingIsRemovedFromFutureMeetingList() {
        Contact contact = addContactgetContact();   // added contact named "Valid"
        Calendar date = Calendar.getInstance();
        date.set(2020, Calendar.OCTOBER, 10);
        int id = test.addFutureMeeting(test.getContacts("V"), date);
        assertFalse(test.getFutureMeetingList(contact).isEmpty());
        date.set(2010, Calendar.OCTOBER, 10);
        test.addMeetingNotes(id, "Notes");
        assertTrue(test.getFutureMeetingList(contact).isEmpty());
    }

    @Test
    public void addMeetingNotesCheckMeetingIsAddedToPastMeetingList() {
        Contact contact = addContactgetContact();   // added contact named "Valid"
        Calendar date = Calendar.getInstance();
        date.set(2020, Calendar.OCTOBER, 10);
        int id = test.addFutureMeeting(test.getContacts("V"), date);
        assertTrue(test.getPastMeetingList(contact).isEmpty());
        date.set(2010, Calendar.OCTOBER, 10);
        test.addMeetingNotes(id, "Notes");
        assertFalse(test.getPastMeetingList(contact).isEmpty());
    }

    @Test
    public void addMeetingNotesCheckGetMeetingBeforeAndAfterShouldHaveTheSameContactsAndDate() {
        addContactgetContact();   // added contact named "Valid"
        Calendar date = Calendar.getInstance();
        date.set(2020, Calendar.OCTOBER, 10);
        int id = test.addFutureMeeting(test.getContacts("V"), date);
        Meeting future = test.getMeeting(id);
        date.set(2010, Calendar.OCTOBER, 10);
        test.addMeetingNotes(id, "Notes");
        Meeting past = test.getMeeting(id);
        assertEquals(future.getContacts(), past.getContacts());
        assertEquals(future.getDate(), past.getDate());
    }

    @Test
    public void convertFutureMeetingToPastMeetingCompareFieldsWithMeeting() {
        addContactgetContact();   // added contact named "Valid"
        Calendar date = Calendar.getInstance();
        date.set(2020, Calendar.OCTOBER, 10);
        int id = test.addFutureMeeting(test.getContacts("V"), date);
        date.set(2010, Calendar.OCTOBER, 10);
        test.addMeetingNotes(id, "Notes");
        Meeting meeting = test.getMeeting(id);
        PastMeeting pastmeeting = test.getPastMeeting(id);
        assertEquals(meeting.getContacts(), pastmeeting.getContacts());
        assertEquals(meeting.getId(), pastmeeting.getId());
        assertEquals(meeting.getDate(), pastmeeting.getDate());
    }

    @Test
    public void addMeetingNotesGetFutureMeetingShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        addContactgetContact();   // added contact named "Valid"
        Calendar date = Calendar.getInstance();
        date.set(2020, Calendar.OCTOBER, 10);
        int id = test.addFutureMeeting(test.getContacts("V"), date);
        date.set(2010, Calendar.OCTOBER, 10);
        test.addMeetingNotes(id, "Notes");
        test.getFutureMeeting(id);
    }

    @Test
    public void addMeetingNotesGetPastMeetingShouldReturnItCheckNotes() {
        addContactgetContact();   // added contact named "Valid"
        Calendar date = Calendar.getInstance();
        date.set(2020, Calendar.OCTOBER, 10);
        int id = test.addFutureMeeting(test.getContacts("V"), date);
        date.set(2010, Calendar.OCTOBER, 10);
        test.addMeetingNotes(id, "Notes added to a Past Meeting");
        assertEquals(test.getPastMeeting(id).getNotes(), "Notes added to a Past Meeting");
    }

    @Test
    public void addMeetingNotesOnPastMeetingCheckNotesAreUpdated() {
        Contact contact = addContactgetContact();
        test.addNewPastMeeting(test.getContacts("V"), getPastDate(), "First set of notes.");
        int id = test.getFutureMeetingList(getPastDate()).get(0).getId();
        test.addMeetingNotes(id, "Some more notes.");
        assertEquals(test.getPastMeeting(id).getNotes(), "First set of notes. Some more notes.");
    }

    @Test
    public void getPastMeetingListWithValidContactFewFutureMeetingsConvertedFewPastMeetingsShouldReturnThemSorted() {
        // add a contact named "Valid"
        Contact contact = addContactgetContact();
        // create five dates
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        Calendar date3 = Calendar.getInstance();
        Calendar date4 = Calendar.getInstance();
        Calendar date5 = Calendar.getInstance();
        date1.set(2020, Calendar.OCTOBER, 10);
        date2.set(2030, Calendar.OCTOBER, 10);
        date3.set(2040, Calendar.OCTOBER, 10);
        date4.set(1999, Calendar.OCTOBER, 9);
        date5.set(2000, Calendar.OCTOBER, 19);
        // set two PastMeetings
        test.addNewPastMeeting(test.getContacts("Valid"), date4, "1999 Meeting");
        int pastId1 = test.getFutureMeetingList(date4).get(0).getId();
        test.addNewPastMeeting(test.getContacts("Valid"), date5, "2000 Meeting");
        // set three FutureMeetings
        int futureId1 = test.addFutureMeeting(test.getContacts("Valid"), date1);
        int futureId2 = test.addFutureMeeting(test.getContacts("Valid"), date2);
        int futureId3 = test.addFutureMeeting(test.getContacts(contact.getId()), date3);
        // change the dates of the FutureMeetings to convert them to PastMeetings
        date1.set(1985, Calendar.OCTOBER, 10);
        date2.set(1985, Calendar.OCTOBER, 11);
        date3.set(1991, Calendar.DECEMBER, 2);
        // convert the three FutureMeetings to PastMeetings
        test.addMeetingNotes(futureId2, "11/10/1985 Meeting");
        test.addMeetingNotes(futureId3, "1991 Meeting");
        test.addMeetingNotes(futureId1, "10/10/1985 Meeting");
        // get the whole five PastMeetings and check the list is sorted by date
        List<PastMeeting> list = test.getPastMeetingList(contact);
        assertEquals(list.size(), 5);
        assertEquals(list.get(0).getNotes(), "10/10/1985 Meeting");
        assertEquals(list.get(1).getNotes(), "11/10/1985 Meeting");
        assertEquals(list.get(2).getNotes(), "1991 Meeting");
        assertEquals(list.get(3).getNotes(), "1999 Meeting");
        assertEquals(list.get(4).getNotes(), "2000 Meeting");
    }

    /* testing flush() ------------------------------------------------------------------------------------------------
    In order to test the flush() method and a correct serialization of data, after each JUnit methods
    the file "Contacts.txt" is deleted (as well as a new instance of ContactManager is created).
    This means that each method is testing a new utilization of ContactManager as started for the first time.
    Tests for flush() are initializing a new ContactManager inside its methods, in order to test a "re-opening"
    of a new instance while keeping the Contact.txt file for retrieving the data saved within the same single test.
    */

    @Test
    public void openContactManagerForTheFirstTimeShouldCreateAFileContactDotTxt() {
        assertFalse(file.exists());
        test.flush();
        assertTrue(file.exists());
    }

    @Test
    public void addContactAndMeetingWithoutFlushingReopenShouldNOTGetThem() {
        addContactsSmith(10);    // add 10 contacts named "Smith"
        int meetingID = test.addFutureMeeting(test.getContacts("Smith"), getFutureDate());
        test = new ContactManagerImpl();    // "open" a new instance of ContactManagerImpl
        assertTrue(test.getContacts("Smith").isEmpty());
        assertNull(test.getFutureMeeting(meetingID));
    }

    @Test
    public void addContactFlushAndReopenShouldGetContactPreviouslyCreatedAndNotGettingAnUnknownOne() {
        addContactsSmith(10);    // add 10 contacts named "Smith"
        int meetingID = test.addFutureMeeting(test.getContacts("Smith"), getFutureDate());
        test.flush();
        test = new ContactManagerImpl();    // "open" a new instance of ContactManagerImpl
        assertFalse(test.getContacts("Smith").isEmpty());
        assertNotNull(test.getFutureMeeting(meetingID));
        assertTrue(test.getContacts("Unknown").isEmpty());
    }

    @Test
    public void AssignContactAndMeetingIdsCloseContactManagerOpenAndAssignOtherCheckTheyAreUnique() {
        Contact valid = addContactgetContact();   // add a contact named "Valid"
        int validID = valid.getId();
        int validMeetingID = test.addFutureMeeting(test.getContacts("Valid"), getFutureDate());
        test.flush();
        test = new ContactManagerImpl();    // "open" a new instance of ContactManagerImpl
        addContactsSmith(1);    // add a contact named "Smith"
        Contact smith = test.getContacts("Smith").toArray(new Contact[1])[0];
        int smithID = smith.getId();
        int smithMeetingID = test.addFutureMeeting(test.getContacts("Smith"), getFutureDate());
        assertNotEquals(validID, smithID);
        assertNotEquals(validMeetingID, smithMeetingID);
    }

}
