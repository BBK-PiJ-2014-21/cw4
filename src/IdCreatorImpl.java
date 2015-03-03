/**
 * Implementation of {@see IdCreator}. 
 * It issues unique IDs, from 1000 upwards (to try and maintain a four-digits standard).
 * 
 * @author federico.bartolomei (BBK-PiJ-2014-21) 
 */
public class IdCreatorImpl implements IdCreator {
    private int id = 999;

    /**
     * {@inheritDoc}
     * 
     * @return an unique id.
     */
    public int createId() {
        id++;
        return id;
    }
    
}    

