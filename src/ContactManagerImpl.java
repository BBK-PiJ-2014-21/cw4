import java.util.*;

/**
 * Implementation of interface {@see ContactManager}
 *
 * @author federico.bartolomei (BBK-PiJ-2014-21)
 */
public class ContactManagerImpl implements ContactManager {
    private IdCreatorImpl IdCreator;
    private Set<Contact> contactSet;
    private List<Meeting> meetings;
    private List<FutureMeeting> futureMeetings;

    /**
     *
     */
    public ContactManagerImpl() {
        IdCreator = new IdCreatorImpl();
        contactSet = new LinkedHashSet<Contact>();
        meetings = new ArrayList<Meeting>();
        futureMeetings = new ArrayList<FutureMeeting>();
    }

    /**
     * {@inheritDoc}
     *
     * This implementation consider a contact unknown/non-existent if it doesn't belong to the set contactSet.
     * This means that the set of contacts passed as argument has to be retrieved from a method call
     * getContacts(String name) or getContacts(int... ids). A set created object of type Contact with an existent and
     * matching id, name and notes
     *
     * @param contacts a list of contacts that will participate in the meeting
     * @param date     the date of which the meeting will take place
     * @return the ID for the meeting
     * @throws IllegalArgumentException if the meeting is set for a time in the past,
     *     or if any contact is unknown / non-existent
     */
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        if(contacts == null || date == null) {
            throw new NullPointerException("Argument cannot be null");
        } else if(!checkContacts(contacts)) {
            throw new IllegalArgumentException("All the contacts for the meeting need to be valid");
        } else if(date.before(Calendar.getInstance())) {
            throw new IllegalArgumentException("Cannot create a FutureMeeting with a past date");
        } else {
            int id = IdCreator.createMeetingId();
            Meeting meeting = new FutureMeetingImpl(date, contacts, id);
            futureMeetings.add((FutureMeeting)meeting);
            meetings.add(meeting);
            return id;
        }
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
     * {@inheritDoc}
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if there is none.
     * @throws IllegalArgumentException if there is a meeting with that ID happening in the past
     */
    @Override
    public FutureMeeting getFutureMeeting(int id) {
        // TODO if(id is in PastMeetingList throw exception)
        for(FutureMeeting m : futureMeetings) {
            if(m.getId()==id) {
                return m;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     */
    @Override
    public Meeting getMeeting(int id) {
        for(Meeting m : meetings) {
            if(m.getId()==id) {
                return m;
            }
        }
        return null;
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

    public boolean checkContacts(Set<Contact> toCheck) {
        for(Contact c : toCheck) {
            if(!contactSet.contains(c)) {
                return false;
            }
        }
        return true;
    }

}
