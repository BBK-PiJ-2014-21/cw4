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
     * This implementation considers a contact unknown if it doesn't belong to the set contactSet.
     * This means that the set of contacts passed as argument should to be retrieved from a method call
     * getContacts(String name) or getContacts(int... ids). In other words, a set containing objects of type
     * Contact with a valid id, name and notes will still not be considered valid unless the same objects
     * are contained in contactSet.
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
     * {@inheritDoc}
     *
     * This implementation considers a contact unknown if the object is not contained into the set contactSet.
     * That is, a Contact object with matching name, id and notes passed as parameter would still throw an
     * exception if it is not a reference of the corresponding Contact in contactSet.
     *
     * @param contact one of the user's contacts
     * @return the list of future meeting(s) scheduled with this contact (maybe empty).
     * @throws IllegalArgumentException if the contact does not exist.
     */
    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        if(contact == null) {
            throw new NullPointerException("Cannot have a null contact");
        } else if(!contactSet.contains(contact)) {
            throw new IllegalArgumentException("The contact doesn't belong to contactSet");
        } else {
            List<Meeting> list = new LinkedList<>();
            for (FutureMeeting f : futureMeetings) {
                if(f.getContacts().contains(contact)) {
                    list.add(f);
                }
            }
            sortMeetingsDate(list);
            return list;
        }
    }

    /**
     * {@inheritDoc}
     *
     * The parameter date will use only the date part (year, month and day) and ignore the time.
     *
     * Returns the list of meetings that are scheduled for, or that took
     * place on, the specified date
     *
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param date the date
     * @return the list of meetings
     */
    @Override
    public List<Meeting> getFutureMeetingList(Calendar date) {
        if (date == null) {
            throw new NullPointerException("Cannot have a null date");
        } else {
            List<Meeting> list = new LinkedList<>();
            if (date.after(Calendar.getInstance())) {
                for (FutureMeeting f : futureMeetings) {
                    if (sameDate(f.getDate(), date)) {
                        list.add(f);
                    }
                }
            } else {
                // TODO deal with pastMeetings
            }
            sortMeetingsDate(list);
            return list;
        }
    }

    /**
     * Sort a list of Meetings by date and time
     *
     * @param list the list to be sorted.
     */
    private void sortMeetingsDate(List<Meeting> list) {
        list.sort((date1, date2) -> date1.getDate().compareTo(date2.getDate()));
    }

    /**
     * Compare the date (year, month, day) of two Calendar instances,
     * without checking the time, and return whether the three fields are equals
     * (i.e. if the two dates are the same, regardless of the time)
     *
     * @param date1 a Calendar date.
     * @param date2 a Calendar date.
     * @return true if the two dates are equals (in terms of year, month and day), false otherwise.
     */
    private boolean sameDate(Calendar date1, Calendar date2) {
        if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)) {
            if (date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)) {
                if (date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH)) {
                    return true;
                }
            }
        }
        return false;
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
