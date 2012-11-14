/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.web.servlets;

/**
 *
 * @author 7
 */
import in.xfiles.core.ejb.LogManagerLocal;
import in.xfiles.core.ejb.SessionManagerLocal;
import in.xfiles.core.entity.User;
import in.xfiles.web.utils.JSFHelper;
import javax.ejb.EJB;
import javax.servlet.http.*;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSession;
import javax.servlet.*;
 
public class SessionListener implements HttpSessionListener {
    private int sessionCount = 0;
 
    @EJB
    private SessionManagerLocal sessionManager;
        
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        synchronized (this) {
            sessionCount++;
        }
       HttpSession session = event.getSession();
//        User user = JSFHelper.getCurrentUser();
//      
//        if(user == null)
//             sessionManager.createUserSession(null, null,null, session.getId());      
//        else{
//             sessionManager.createUserSession(user.getUserId(), null,null, session.getId());    
//        }
        System.out.println("Session Created: " + session.getId());
        System.out.println("Total Sessions: " + sessionCount);
    }
 
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        synchronized (this) {
            sessionCount--;
        }
          HttpSession session = event.getSession();
          sessionManager.deleteSession(session);
        System.out.println("Session Destroyed: " + event.getSession().getId());
        System.out.println("Total Sessions: " + sessionCount);
    }
    
   
    
}
