/**
 * Implementation of interface {@see Contact}.
 * This class does check the uniqueness of the ID. This has to be guaranteed by the class which issues
 * the numbers, or by the class above that. This implementation just accepts any number given at construction
 * time as final.
 * 
 * @author federico.bartolomei (BBK-PiJ-2014-21)
 */
public class ContactImpl implements Contact {
    private String name;
    private final int id;
    private String notes;

    /**
     * Constructor for a new contact, with name, id and notes.
     *
     * @param name the name of the contact.
     * @param id the unique id of the contact.
     * @param notes the notes about the contact.
     * @throws NullPointerException if name or notes are null.
     */
    public ContactImpl(String name, int id, String notes) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        } else if (notes == null) {
            throw new NullPointerException("Notes cannot be null");
        } else {
            this.name = name;
            this.id = id;
            this.notes = notes;
        }
    }
    
    /**
     * Constructor for a new contact, with name and id. 
     *  
     * @param name the name of the contact.
     * @param id the unique id of the contact.
     * @throws NullPointerException if name is null.
     */
    public ContactImpl(String name, int id) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        } else {
            this.name = name;
            this.id = id;
            notes = "";
        }
    }

    /**
     * {@inheritDoc}
     *  
     * @return the name of the contact.
     */
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @return the unique id of the contact.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * {@inheritDoc} 
     * 
     * @return the notes about the contact to be retrieved.
     */
    @Override
    public String getNotes() {
        return notes;
    }

    /**
     * {@inheritDoc}
     *
     * @param notes the notes about the contact to be added.
     * @throws NullPointerException if notes is null.
     */
    @Override
    public void addNotes(String notes) {
        if (notes == null) {
            throw new NullPointerException("Notes cannot be null");
        } else {
            if (this.notes.equals("")) {
                this.notes = notes;
            } else {
                this.notes += " " + notes;
            }
        }
    }
    
}
