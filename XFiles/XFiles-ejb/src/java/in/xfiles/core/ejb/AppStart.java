package in.xfiles.core.ejb;

import java.net.URL;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 * @author danon
 */
@Singleton
@LocalBean
@Startup
public class AppStart {

    private static Logger log = null;
    
    @PostConstruct
    public void startup()
    {
        initLog4j();
    }
    
    @PreDestroy
    public void shutdown() {
    }

    private void initLog4j() {
        
    }
    
    private void log4jInit() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource("/log4j.xml");

        DOMConfigurator.configure(url.getFile());
        log = Logger.getLogger(Startup.class);
        if (log != null) {
            log.debug("Logger initialized with " + url);
        }
    }

}
