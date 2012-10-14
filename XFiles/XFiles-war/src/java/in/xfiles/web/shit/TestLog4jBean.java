package in.xfiles.web.shit;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;

/**
 * Just test our log4j
 * @author danon
 */
@ManagedBean
@RequestScoped
public class TestLog4jBean {

    private static final Logger log = Logger.getLogger(TestLog4jBean.class); 
    
    /**
     * Creates a new instance of TestLog4jBean
     */
    public TestLog4jBean() {
        log.trace("Constructor!!!");
    }
    
    public String getA() {
        log.info("getA!!!");
        return "";
    }
}
