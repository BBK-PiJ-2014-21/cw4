import java.util.Calendar;
import java.util.Set;

/**
 * Implementation of interface {@see Meeting}. 
 * This class does not check the uniqueness of the ID. This has to be guaranteed by the class which issues 
 * the numbers, or by the class above that. This implementation just accepts any number given at construction
 * time as final.
 *
 * @author federico.bartolomei (BBK-PiJ-2014-21)
 */
public class MeetingImpl implements Meeting {
    private Calendar date;
    private Set<Contact> contacts;
    private final int id;
    
    public MeetingImpl(Calendar date, Set<Contact> contacts, int id) {
        this.date = date;
        this.contacts = contacts;
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public Calendar getDate() {
        return date;
    }
    
    public Set<Contact> getContacts() {
        return contacts;
    }
    
}
