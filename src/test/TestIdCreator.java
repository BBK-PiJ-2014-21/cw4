package test;

import main.IdCreator;
import main.IdCreatorImpl;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JUnit test class for interface {@see IdCreator}. It assumes an implementation called IdCreatorImpl.
 * 
 * @author federico.bartolomei (BBK-PiJ-2014-21)
 */
public class TestIdCreator {
    
    public int[] createIdArray(int n) {
        IdCreator id = new IdCreatorImpl();
        int[] array = new int[n];
        for(int i=0; i<array.length; i++) {
            array[i] = id.createId();
        }
        return array;
    }
    
    @Test
    public void test100IDsAreUnique() {
        int[] array = createIdArray(100);
        for(int i=0; i<array.length; i++) {
            for(int j=i+1; j<array.length; j++) {
                assertTrue("id at position " + i + "= id at position " + j, array[i] != array[j]);
            }
        }
    }
    
    @Test
    public void testTwoArraysOfIDsCreatedWithTwoInstancesOfIdCreatorShouldHaveTheSameIDs() {
        int[] first = createIdArray(10);
        int[] second = createIdArray(10);
        for(int i=0; i<first.length; i++) {
            for (int j=0; j<second.length; j++) {
                assertFalse(first[i] + "!=" + second[j], first[i]!=second[j]);
            }
        }
        
    }
    
    
}
