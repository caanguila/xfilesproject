package in.xfiles.core.helpers;

/**
 *
 * @author danon
 */
public class StringUtils {
    
    public static final String EMPTY_STRING = "";
    
    public static String getValidString(Object o, String defaultValue) {
        if(o==null)
            return defaultValue;
        return o.toString();
    }
    
    public static String getValidString(Object o) {
        return getValidString(o, EMPTY_STRING);
    }

    public static boolean isEmpty(String s) {
        return getValidString(s).isEmpty();
    }
}
