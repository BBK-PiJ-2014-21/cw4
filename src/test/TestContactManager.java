import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Null;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 *
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
    ExpectedException exception = ExpectedException.none();

    public void addContacts(int n) {
        for (int i = 1; i <=n; i++) {
            test.addNewContact("Contact" + i, "Notes about Contact" + i);
        }
    }

    public void addContactsSmith(int n) {
        for (int i = 1; i<n; i++) {
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
        assertEquals(byNameshouldBeSize1.size(), 1);
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
        int id2 = ((Contact)test.getContacts("Contact2").toArray()[1]).getId();
        assertNotEquals(id1, id2);
    }

    @Test
    public void testAdd10ContactsWhoseNameContainsSameStringGetByName() {
        addContacts(10);
        Set<Contact> list = test.getContacts("Contact");
        assertEquals(list.size(), 10);
        for(Contact contact : list) {
            contact.getName().contains("Contact");
        }
    }

    @Test
    public void testAdd10ContactsWhoseNameIsSameStringPlusOtherThatDoesntGetByName() {
        addContacts(5);
        addContactsSmith(10);
        Set<Contact> list = test.getContacts("Smith");
        assertEquals(list.size(), 10);
        for(Contact contact : list) {
            contact.getName().equals("Smith");
        }
    }

    @Test
    public void testAddMoreNewContactsWhoseNameContainsSameStringGetByNameCheckAllIDsAreUnique() {
        addContacts(10);
        addContacts(10);
        Contact[] list = (Contact[])test.getContacts("Contact").toArray();
        for(int i=0; i<list.length; i++) {
            for(int j=i+1; j<list.length; j++) {
                assertNotEquals(list[i].getId(), list[j].getId());
            }
        }
    }

    @Test
    public void testAddMoreContactsWithSomeDifferentSomeSameNameCombineSetsAndCheckAllIDsAreUnique() {
        addContacts(5);
        addContactsSmith(5);
        Set<Contact> different = test.getContacts("Contact");
        Set<Contact> same = test.getContacts("Smith");
        Set<Contact> all = new HashSet<Contact>();
        all.addAll(different);
        all.addAll(same);
        Contact[] list = (Contact[])all.toArray();
        for(int i = 0; i < list.length; i++) {
            for(int j = i+1; i < list.length; i++) {
                assertNotEquals(list[i].getId(), list[j].getId());
            }
        }
    }

    @Test
    public void testAddMoreNewContactsWhoseNameIsSameStringGetByNameCheckAllIDsAreUnique() {
        addContactsSmith(10);
        addContactsSmith(5);
        Contact[] list = (Contact[])test.getContacts("Smith").toArray();
        for(int i=0; i<list.length; i++) {
            for(int j=i+1; j<list.length; j++) {
                assertNotEquals(list[i].getId(), list[j].getId());
            }
        }
    }

    @Test
    public void testAddMoreNewContactsGetAllContactsByNameAndByArrayOfIDsCompareSets() {
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
        int validID = ((Contact[])test.getContacts("Contact1").toArray())[0].getId();
        test.getContacts(validID, 12312);
    }

    @Test
    public void testGetContactEmptyStringShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        test.getContacts("");
    }

    @Test
    public void testGetContactNullShouldThrowNullPointerException() {
        exception.expect(NullPointerException.class);
        String nullString = null;
        test.getContacts(nullString);
    }

    /**
     * IllegalArgumentException for a getName(String) not matching with the list of Contacts created
     * is not a requirement in the interface.
     */
    @Test
    public void testGetContactNameNotInTheListShouldThrowIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        test.getContacts("Name not in the list");
    }


}
