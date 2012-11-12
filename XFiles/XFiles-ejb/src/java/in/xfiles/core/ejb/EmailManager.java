package in.xfiles.core.ejb;

import javax.ejb.Stateless;
import org.apache.log4j.Logger;

/**
 * Provides basic routines for sending and receiving messages.
 * 
 * @author danon
 */
@Stateless
public class EmailManager implements EmailManagerLocal {

    private final Logger log = Logger.getLogger(EmailManager.class);
    
//    public void sendEmail(EmailObject email) {
//        
//    }

}
