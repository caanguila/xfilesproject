/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.UserSession;
import javax.ejb.Local;

/**
 *
 * @author 7
 */
@Local
public interface SessionManagerLocal {
    UserSession getUserSession(Long userId);
    UserSession createUserSession(Long userId, String ip);
    UserSession createUserSession(Long userId, String ip, String brouser);
    
}
