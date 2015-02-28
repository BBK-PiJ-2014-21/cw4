package main;

/**
 * Interface to issue unique IDs, created to support the implementation of {@see Contact}.
 * 
 * @author federico.bartolomei (BBK-PiJ-2014-21)
 */
public interface IdCreator {

    /**
     * Create an unique ID number. The implementation should guarantee that a different number
     * is returned every time the method is called.
     *
     * @return an unique id number.
     */
    public int createId();
    
}
