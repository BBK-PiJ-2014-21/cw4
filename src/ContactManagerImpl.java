import java.util.*;

/**
 * Implementation of interface {@see ContactManager}
 *
 * @author federico.bartolomei (BBK-PiJ-2014-21)
 */
public class ContactManagerImpl implements ContactManager {
    private IdCreatorImpl IdCreator;
    private Set<Contact> contactSet;

    /**
     *
     */
    public ContactManagerImpl() {
        IdCreator = new IdCreatorImpl();
        contactSet = new LinkedHashSet<Contact>();
    }

    /**
     *
     *
     * @param contacts a list of contacts that will participate in the meeting
     * @param date     the date of which the meeting will take place
     * @return
     */
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        return -1; // TODO
    }

    /**
     *
     *
     * @param id the ID for the meeting
     * @return
     */
    @Override
    public PastMeeting getPastMeeting(int id) {
        return null; // TODO
    }

    /**
     *
     *
     * @param id the ID for the meeting
     * @return
     */
    @Override
    public FutureMeeting getFutureMeeting(int id) {
        return null; // TODO
    }

    /**
     *
     *
     * @param id the ID for the meeting
     * @return
     */
    @Override
    public Meeting getMeeting(int id) {
        return null; // TODO
    }

    /**
     *
     *
     * @param contact one of the user's contacts
     * @return
     */
    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        return null; // TODO
    }

    /**
     *
     *
     * @param date the date
     * @return
     */
    @Override
    public List<Meeting> getFutureMeetingList(Calendar date) {
        return null; // TODO
    }

    /**
     *
     *
     * @param contact one of the user’s contacts
     * @return
     */
    @Override
    public List<PastMeeting> getPastMeetingList(Contact contact) {
        return null; // TODO
    }

    /**
     *
     *
     * @param contacts a list of participants
     * @param date the date on which the meeting took place
     * @param text messages to be added about the meeting.
     */
    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        // TODO
    }

    /**
     *
     *
     * @param id the ID of the meeting
     * @param text messages to be added about the meeting.
     */
    @Override
    public void addMeetingNotes(int id, String text) {
        // TODO
    }

    /**
     * {@inheritDoc}
     *
     * @param name  the name of the contact.
     * @param notes notes to be added about the contact.
     * @throws NullPointerException if the name or the notes are null.
     */
    @Override
    public void addNewContact(String name, String notes) {
        if(name == null || notes == null) {
            throw new NullPointerException("Argument cannot be null");
        } else {
            int id = IdCreator.createContactId();
            contactSet.add(new ContactImpl(name, id, notes));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param ids an arbitrary number of contact IDs
     * @return a list containing the contacts that correspond to the IDs
     * @throws IllegalArgumentException if any of the IDs do not correspond to a real contact
     */
    @Override
    public Set<Contact> getContacts(int... ids) {
        Set<Contact> contacts = new LinkedHashSet<Contact>();
        for(int id : ids) {
            boolean found = false;
            for(Contact c : contactSet) {
                if (c.getId() == id) {
                    contacts.add(c);
                    found = true;
                }
            }
            if(!found) {
                throw new IllegalArgumentException(id + " do not correspond to a real contact");
            }
        }
        return contacts;
    }

    /**
     * {@inheritDoc}
     *
     * If the name do not correspond to a real contact, an empty Set will be returned.
     *
     * @param name the string to search for
     * @return a list with the contacts whose name contains that string
     * @throws NullPointerException if the parameter is null
     */
    @Override
    public Set<Contact> getContacts(String name) {
        Set<Contact> contacts = new LinkedHashSet<Contact>();
        if(name == null || name.equals("")) {
            throw new NullPointerException("Name of the contact to get cannot be null or empty");
        }
        for(Contact c : contactSet) {
            if(c.getName().contains(name)) {
                contacts.add(c);
            }
        }
        return contacts;
    }

    /**
     *
     *
     */
    @Override
    public void flush() {
        // TODO
    }

}
