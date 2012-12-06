package in.xfiles.core.helpers;

import java.util.Collection;
import java.util.ArrayList;

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
    
    public static ArrayList<String> parceElements(String target){
        ArrayList result = new ArrayList();
        
       
        String[] parts1 = target.split(":");
        for(String p1: parts1){            
            String[] parts2 = p1.split("=");
            for(int i=0; i<parts2.length; i++){
                
                if(parts2[i].equals("user_id")){
                    if(i+1<parts2.length){
                        String value = parts2[i+1];
                        result.add("user_id");
                        result.add(value);
                    }
                }
                if(parts2[i].equals("request_id")){
                    if(i+1<parts2.length){
                        String value = parts2[i+1];
                        result.add("request_id");
                        result.add(value);
                    }
                }
                if(parts2[i].equals("login")){
                    if(i+1<parts2.length){
                        String value = parts2[i+1];
                        result.add("login");
                        result.add(value);
                    }
                }
                if(parts2[i].equals("password")){
                    if(i+1<parts2.length){
                        String value = parts2[i+1];
                        result.add("password");
                        result.add(value);
                    }
                }
                
            }
        }
        
        return result;
    }
}
