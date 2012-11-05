package in.xfiles.core.ejb;


import in.xfiles.core.entity.User;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.apache.log4j.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author danon
 */
@Singleton
@LocalBean
@Startup
public class AppStart {
    @PersistenceContext
    private EntityManager entityManager;
    
    private static final Logger log = Logger.getLogger(AppStart.class);
    
    @PostConstruct
    public void startup()
    {
        log.info("Application Initialized");
    }
    
    @PreDestroy
    public void shutdown() {
        log.info("Application is about to shutdown");
       
    }
    
//    private void log4jInit() {
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        URL url = classLoader.getResource("/log4j.xml");
//
//        DOMConfigurator.configure(url.getFile());
//        log = Logger.getLogger(Startup.class);
//        if (log != null) {
//            log.debug("Logger initialized with " + url);
//        }
//    }

}
