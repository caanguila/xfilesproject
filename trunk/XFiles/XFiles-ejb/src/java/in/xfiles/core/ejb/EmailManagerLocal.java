/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import javax.ejb.Local;

/**
 *
 * @author danon
 */
@Local
public interface EmailManagerLocal {
 
    void sendSimpleEmail(String subject, String text, String ... recepients);
    
}
