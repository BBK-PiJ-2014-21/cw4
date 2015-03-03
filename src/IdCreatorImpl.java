/**
 * Implementation of {@see IdCreator} interface. It creates ID numbers for a creation 
 * of a new Meeting and Contact. Classes using this implementation should make sure
 * to use the same unique object in order to have a different id number for any method call,
 * as the class is not static.
 * 
 * @author federico.bartolomei (BBK-PiJ-2014-21) 
 */
public class IdCreatorImpl implements IdCreator {
    private int contactID = 999;
    private int meetingID = 0;

    /**
     * {@inheritDoc}
     * 
     * Creates a new unique id to be associated with a Contact.
     * It issues numbers from 1000 upwards (to try and maintain a four-digits standard).
     * 
     * @return an unique id for a new Contact.
     */
    @Override
    public int createContactId() {
        contactID++;
        return contactID;
    }

    /**
     * {@inheritDoc}
     * 
     * Creates a new unique id to be associated with a Meeting.
     * It issues numbers from 1 upwards. 
     *   
     * @return an unique id for a new Meeting.
     */
    @Override
    public int createMeetingId() {
        meetingID++;
        return meetingID;
    }
    
    
}    

