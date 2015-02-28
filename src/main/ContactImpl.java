package main;

/**
 * Implementation of interface {@see Contact}.
 */
public class ContactImpl implements Contact {
    private String name;
    private final int id;  // TODO it has to be an unique id, need to create code to guarantee that.
    private String notes;

    /**
     * Constructor for a new contact, with name, id and notes.
     *
     * @param name the name of the contact.
     * @param id the unique id of the contact.
     * @param notes the notes about the contact.
     */
    public ContactImpl(String name, int id, String notes) {
        this.name = name;
        this.id = id;
        this.notes = notes;
    }
    
    /**
     * Constructor for a new contact, with name and id. 
     *  
     * @param name the name of the contact.
     * @param id the unique id of the contact.
     */
    public ContactImpl(String name, int id) {
        this.name = name;
        this.id = id;
        notes = "";
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
     */
    @Override
    public void addNotes(String notes) {
        if(this.notes.equals("")) {
            this.notes = notes;
        } else {
            this.notes += " " + notes;
        }
    }
    
}
