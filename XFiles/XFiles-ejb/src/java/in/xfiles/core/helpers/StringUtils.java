package in.xfiles.core.helpers;

import java.nio.charset.Charset;

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
    
    public static boolean isTrue(String s) {
        return isTrue(s, "yes", "true", "1", "ok", "on");
    }
    
    public static boolean isTrue(String s, String... accept) {
        final String v = getValidString(s);
        for(String t : accept) {
            if(v.equalsIgnoreCase(t))
                return true;
        }
        return false;
    }
    
    public static boolean isFalse(String s) {
        return !isTrue(s);
    }
    
    public static String decode(String value, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        if(value == null) {
            return null;
        }
        return new String(value.getBytes(charset));
    }
    
    public static String convertEncodings(String value, String srcCharset, String dstCharset) {
        return decode(decode(value, srcCharset), dstCharset);
    }
}
