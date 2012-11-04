package in.xfiles.core.helpers;

import java.util.Collection;

/**
 * This class should contain different useful methods.
 * 
 * @author danon
 */
public abstract class CommonTools {
    
    /**
     * Null-safe method for getting the first element from collection.
     * @param c source collection
     * @return first method of collection, or null if there is no elements
     */
    public static <T> T getFirstElement(Collection<T> c) {
        if(c == null || c.isEmpty())
            return null;
        return c.iterator().next();
    } 
    
    /**
     * Returns single element from collection.
     * @param c source collection
     * @return first element of collection, or null when it size != 1
     */
    public static <T> T getSingleElement(Collection<T> c) {
        if(c == null || c.size() != 1)
            return null;
        return c.iterator().next();
    }
}
