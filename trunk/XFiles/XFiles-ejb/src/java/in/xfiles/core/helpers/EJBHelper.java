/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.helpers;

import in.xfiles.core.ejb.AppStart;
import java.util.logging.Level;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
 *
 * @author 7
 */
public class EJBHelper {
    private static final Logger log = Logger.getLogger(EJBHelper.class);
    private static InitialContext initialContext;
    
    private EJBHelper(){
        try {
            initialContext = new InitialContext();
        } catch (NamingException ex) {
            log.error("Can't initialise InitialContext");
        }
    }
    
    
    private static class EJBHelperHolder{
        private final static EJBHelper INSTANSE = new EJBHelper();
    }
    
    public static EJBHelper getInstance(){
        return EJBHelperHolder.INSTANSE;
    }
    
    public static Object findBeanByName(String name){
    try{
        if(initialContext instanceof InitialContext){
        return initialContext.lookup(name);
        }else{
            log.error("initialContext is null");
            return null;
        }
        
    }catch(NamingException exception){
        log.error("Can't find EJB by name: "+name);
        log.error(exception);
    }
    
    return null;
    }
    
}
