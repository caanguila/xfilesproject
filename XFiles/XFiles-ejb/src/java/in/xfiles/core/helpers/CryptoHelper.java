package in.xfiles.core.helpers;

import java.security.MessageDigest;
import org.apache.log4j.Logger;

/**
 *
 * @author 7
 */
public class CryptoHelper {
    
    private static final Logger log = Logger.getLogger(CryptoHelper.class);
    
    public static String encryptString(String s, String algo)
    {
        try {
             MessageDigest md = MessageDigest.getInstance(algo);
             md.update(s.getBytes());
             s = null;
             return toHexString(md.digest());
         } catch (Exception ex) {
             log.error("Couldn't calculate MD5 of string", ex);
             throw new RuntimeException("Couldn't calculate MD5 of string", ex);
         }
    } 
    
    private static String toHexString(byte[] byteData) {
        StringBuilder hexString = new StringBuilder();
             for (int i = 0; i < byteData.length; i++) {
                 String hex = Integer.toHexString(0xff & byteData[i]);
                 if (hex.length() == 1) {
                     hexString.append('0');
                 }
                 hexString.append(hex);
             }
             return hexString.toString();
    }
    
    public static String SHA256(String s) {
        return encryptString(s, "SHA-256");
    }
    
    public static String MD5(String s) {
        return encryptString(s, "MD5");
    }
}
