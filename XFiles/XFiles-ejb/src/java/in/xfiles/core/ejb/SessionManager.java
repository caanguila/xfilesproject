/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.UserSession;
import in.xfiles.core.entity.User;
import in.xfiles.core.helpers.CommonConstants;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import java.util.Date;
/**
 *
 * @author 7
 */
@Stateless
@LocalBean
public class SessionManager implements SessionManagerLocal{
    private static final Logger log = Logger.getLogger(SessionManager.class); 
      @PersistenceContext
      private EntityManager entityManager;
      
    @EJB
    private LogManagerLocal logManager;

    @Override
    public UserSession getUserSession(Long userId) {
        
       return getCurrentUserSession(userId);
    }

    @Override
    public UserSession createUserSession(Long userId, String ip) {
        
        User user = null;
        if(userId != null){
             user = entityManager.find(User.class, userId);
        }
            UserSession userSession = new UserSession();
            userSession.setUserId(user);
            userSession.setIpAdress(ip);
            
            entityManager.persist(userSession);
            log.info("Session "+userSession+"  created for user: "+user);
            logManager.addRecord(userId, CommonConstants.SESSION_CREATED, "", "", "");

        
        return null;
                
    }

    @Override
    public UserSession createUserSession(Long userId, String ip, String brouser, String session) {
        User user = null;
        if(userId != null){
             user = entityManager.find(User.class, userId);
        }
        
            UserSession userSession = new UserSession();
            userSession.setUserId(user);
            userSession.setIpAdress(ip);
            userSession.setBrouser(brouser);
            userSession.setSession(session);
            entityManager.persist(userSession);
            log.info("Session "+userSession+"  created for user: "+user);
            logManager.addRecord(userId, CommonConstants.SESSION_CREATED, "", "", session);
       
              
        
        return null;
    }
    
    private UserSession getCurrentUserSession(Long userId){
        Query query = entityManager.createQuery("select q from UserSession q where q.userId =:userId");
        User user = entityManager.find(User.class, userId);
        if(user == null) {
            log.warn("User id not found");
            return null;
        }        
        query.setParameter("userId", user);
        List<UserSession> list = query.getResultList();
        UserSession current = null;
        if(!list.isEmpty()){
            for(UserSession elem: list){
                if(current == null) current = elem;
                if(elem.getSessionId().compareTo(current.getSessionId())>0) current = elem;
            }
            return current;
            
        }else{
            log.warn("User id not found in user session table!");
        return null;
        }
    }
    
//    public HttpSession getUserSession(BigInteger usetId){
//        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);  
//    return null;
//    }

    @Override
    public UserSession getUserSessionBySessionName(String session) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void modifySession(HttpSession session, Long userId, String ip, String brouser, String sessionName) {
       String sessionId = session.getId();
       Query q = entityManager.createQuery("select u from UserSession u where u.session =:sessionName");
       q.setParameter("sessionName", sessionId);
       List result = q.getResultList();
//       log.info("Session: "+result.iterator().next());
       if(result.isEmpty()){
           log.info("Session is null in database");
           createUserSession(userId, ip, brouser, sessionId);
           
            logManager.addRecord(userId, CommonConstants.SESSION_CREATED, "", "", sessionId);
       }else{
           log.info("Session already created: merge");
           UserSession userSession = (UserSession) result.iterator().next();
           User user = entityManager.find(User.class, userId);
           
               userSession.setUserId(user);
               userSession.setIpAdress(ip);
               userSession.setBrouser(brouser);
               userSession.setSession(sessionName);
               entityManager.merge(userSession);
               
               logManager.addRecord(userId, CommonConstants.SESSION_MODIFYED, "", "", sessionId);
              
                 
    }
       
    }

    @Override
    public void deleteSession(HttpSession session) {
        String sessionId = session.getId();
       Query q = entityManager.createQuery("select u from UserSession u where u.session =:sessionName");
       q.setParameter("sessionName", sessionId);
       List result = q.getResultList();
//       log.info("Session: "+result.iterator().next());
       if(result.isEmpty()){
           log.info("Can't find session with id: "+sessionId);
           
       }else{
           UserSession ses = (UserSession) result.iterator().next();
           User user = ses.getUserId();
           entityManager.remove(ses);
           log.info("Session succesfuly removed from database");
          
           if(user != null)
           logManager.addRecord(user.getUserId(), CommonConstants.SESSION_DESTROYED, "", "", sessionId);
           else{
           logManager.addRecord(null, CommonConstants.SESSION_DESTROYED, "", "", sessionId);     
           }
           //logManager.addRecord(userId, CommonConstants.SESSION_CREATED, sessionId, sessionId);
       }
    }
    
    
}
