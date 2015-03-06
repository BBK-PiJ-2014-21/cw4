import java.util.Calendar;
import java.util.Set;

/**
 * Implementation of interface {@see PastMeeting}.
 *
 * @author federico.bartolomei (BBK-PiJ-2014-21)
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
    private String notes;

    /**
     * Constructor for a past meeting without any notes.
     * It will pass the arguments the the parent class Meeting, and the values of the notes
     * will be the empty String.
     *
     * @param date the date of the meeting
     * @param contacts the contacts that attended the meeting
     * @param id the unique-id of the meeting
     */
    public PastMeetingImpl(Calendar date, Set<Contact> contacts, int id) {
        super(date, contacts, id);
        notes = "";
    }

    /**
     * Constructor for a past meeting with notes.
     *
     * @param date the date of the meeting
     * @param contacts the contacts that attended the meeting
     * @param id the unique-id of the meeting
     * @param notes the notes about the meeting
     * @throws NullPointerException for an attempt to add null notes
     */
    public PastMeetingImpl(Calendar date, Set<Contact> contacts, int id, String notes) {
        super(date, contacts, id);
        if (notes == null) {
            throw new NullPointerException("Cannot add null notes");
        } else {
            this.notes = notes;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the notes about the past meeting
     */
    @Override
    public String getNotes() {
        return notes;
    }

    /**
     * Add some notes about the past meeting.
     *
     * @param newNotes the new notes to be added about the meeting
     * @throws NullPointerException for an attempt to add null notes
     */
    public void addNotes(String newNotes) {
        if(newNotes==null) {
            throw new NullPointerException("Cannot add null notes");
        } else {
            if(notes.equals("")) {
                notes = newNotes;
            } else {
                notes += " " + newNotes;
            }
        }
    }

}
