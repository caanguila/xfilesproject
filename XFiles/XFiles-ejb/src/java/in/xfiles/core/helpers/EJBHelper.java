package in.xfiles.core.helpers;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;

/**
 * Singleton for loading EJB by class name and JNDI name.
 *
 */
public class EJBHelper {

    private final Logger log = Logger.getLogger(EJBHelper.class);
    private final InitialContext initialContext;
    private static final String EJB_MODULE_PATH = "java:global/XFiles/XFiles-ejb/";
    private static final String EJB_PACKAGE_NAME = "in.xfiles.core.ejb";

    private EJBHelper() {
        try {
            initialContext = new InitialContext();
        } catch (NamingException ex) {
            log.error("Can't initialise InitialContext");
            throw new RuntimeException("Can't initialise InitialContext", ex);
        }
    }

    public static EJBHelper getInstance() {
        return EJBHelperHolder.INSTANSE;
    }
    
    public EntityManager getEntityManager() throws NamingException {
        //return resolve("java:comp/env/persistence/em", EntityManager.class); 
        return Persistence.createEntityManagerFactory("XFiles-ejbPU").createEntityManager();
    }

    public <T> T resolve(String jndi, Class<T> clazz) throws NamingException {
        return (T) initialContext.lookup(jndi);
    }

    public <T> T getBean(Class<T> clazz) {
        return (T) findBeanByName(clazz.getSimpleName());
    }

    private Object findBeanByName(String name) {
        try {
            if (initialContext != null) {
                return resolve(getBeanPath(name), Object.class);
            } else {
                log.error("initialContext is null");
                return null;
            }

        } catch (NamingException exception) {
            log.error("Can't find EJB by name: " + name, exception);
        }

        return null;
    }

    private String getBeanPath(String name) {
        final String beanName = (String) name.substring(0, name.length() - "Local".length());
        return EJB_MODULE_PATH + beanName + "!" + EJB_PACKAGE_NAME + "." + name;
    }

    private static class EJBHelperHolder {

        private final static EJBHelper INSTANSE = new EJBHelper();
    }
}
