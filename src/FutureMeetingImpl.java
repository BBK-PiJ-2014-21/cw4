import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

/**
 *
 */
public class FutureMeetingImpl extends MeetingImpl implements Serializable, FutureMeeting {
    private static final long serialVersionUID = 1L;

    public FutureMeetingImpl(Calendar date, Set<Contact> contacts, int id) {
        super(date, contacts, id);
    }

}
