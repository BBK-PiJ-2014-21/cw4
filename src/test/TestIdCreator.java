import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
            array[i] = id.createContactId();
        }
        return array;
    }
    
    @Test
    public void test100ContactIDsAreUnique() {
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
        for (int i = 0; i < first.length; i++) {
            for (int j = 0; j < second.length; j++) {
                assertFalse(first[i] + "!=" + second[j], first[i] != second[j]);
            }
        }

    }
        
    @Test
    public void testMeetingId() {
        IdCreator test = new IdCreatorImpl();
        Set<Integer> meetingIDs = new HashSet<Integer>();
        for(int i=0; i<9; i++) {
            meetingIDs.add(test.createMeetingId());
        }
        assertEquals(test.createMeetingId(), 10);
    }
    
    
}
