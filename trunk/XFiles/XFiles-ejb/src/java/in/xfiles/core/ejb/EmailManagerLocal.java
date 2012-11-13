package in.xfiles.core.ejb;

import javax.ejb.Local;

/**
 *
 * @author danon
 */
@Local
public interface EmailManagerLocal {
 
    void sendSimpleEmail(String subject, String text, String ... recepients);
    
    void sendEmail(String subject, String text, String ... recepients);
    
}
