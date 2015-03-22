import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

/**
 * Implementation of interface {@see Meeting}.
 * This class does check the uniqueness of the Meeting ID. This is guaranteed by the class which issues
 * the numbers, and managed by the class above that (@see ContactManagerImpl). In this sense this
 * class is dependant on the ContactManager. This implementation just accepts any number given
 * at construction time as final. This is to make conversions between Meetings easier, being the ContactManager
 * able to move and re-assign ID numbers.
 *
 * @author federico.bartolomei (BBK-PiJ-2014-21)
 */
public class MeetingImpl implements Serializable, Meeting {
    private static final long serialVersionUID = 1L;
    private Calendar date;
    private Set<Contact> contacts;
    private final int id;
    /**
     * Constructor for a new Meeting.
     *
     * @param date the date of the meeting.
     * @param contacts the contacts that attended the meeting
     * @param id the unique-id of the meeting
     * @throws NullPointerException if the date is null
     * @throws NullPointerException if the list of contacts is null
     * @throws IllegalArgumentException if the list of contacts is empty
     */
    public MeetingImpl(Calendar date, Set<Contact> contacts, int id) {
        if(date == null) {
            throw new NullPointerException("Date cannot be null");
        } else if(contacts == null) {
            throw new NullPointerException("Set<Contact> cannot be null");
        } else if(contacts.isEmpty()) {
            throw new IllegalArgumentException("Set<Contact> must contain a minimum of one contact");
        }
        this.date = date;
        this.contacts = contacts;
        this.id = id;
    }

    /**
     * {@inheritDoc}
     *
     * @return the unique-id of the meeting
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     *
     * @return the date of the meeting
     */
    @Override
    public Calendar getDate() {
        return date;
    }

    /**
     * {@inheritDoc}
     *
     * @return the list of contacts that attended the meeting
     */
    @Override
    public Set<Contact> getContacts() {
        return contacts;
    }
    
}
