/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.UserSession;
import javax.ejb.Local;
import javax.servlet.http.HttpSession;

/**
 *
 * @author 7
 */
@Local
public interface SessionManagerLocal {
    UserSession getUserSession(Long userId);
    UserSession getUserSessionBySessionName(String session);
    UserSession createUserSession(Long userId, String ip);
    UserSession createUserSession(Long userId, String ip, String brouser, String session);
    void modifySession(HttpSession session, Long userId, String ip, String brouser, String sessionName);
    void deleteSession(HttpSession session);
}
