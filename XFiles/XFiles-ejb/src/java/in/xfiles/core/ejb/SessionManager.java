/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.UserSession;
import in.xfiles.core.entity.User;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author 7
 */
@Stateless
@LocalBean
public class SessionManager implements SessionManagerLocal{
    private static final Logger log = Logger.getLogger(LogManager.class); 
      @PersistenceContext
      private EntityManager entityManager;

    @Override
    public UserSession getUserSession(Long userId) {
        
       return getCurrentUserSession(userId);
    }

    @Override
    public UserSession createUserSession(Long userId, String ip) {
        
        User user = entityManager.find(User.class, userId);
        if(user instanceof User && ip instanceof String){
            UserSession userSession = new UserSession();
            userSession.setUserId(user);
            userSession.setIpAdress(ip);
            
            entityManager.persist(userSession);
            log.info("Session "+userSession+"  created for user: "+user);
            
        }else{
            log.warn("User or Ip is null; Can't create Session");
        }
        
        return null;
                
    }

    @Override
    public UserSession createUserSession(Long userId, String ip, String brouser) {
         User user = entityManager.find(User.class, userId);
        if(user instanceof User && ip instanceof String){
            UserSession userSession = new UserSession();
            userSession.setUserId(user);
            userSession.setIpAdress(ip);
            userSession.setBrouser(brouser);
            
            entityManager.persist(userSession);
            log.info("Session "+userSession+"  created for user: "+user);
            
        }else{
            log.warn("User or Ip is null; Can't create Session");
        }
        
        return null;
    }
    
    private UserSession getCurrentUserSession(Long userId){
        Query query = entityManager.createQuery("select q from UserSession q where q.userId =:userId");
        query.setParameter("userId", userId);
        List<UserSession> list = query.getResultList();
        if(!list.isEmpty()){
            UserSession session = list.get(0);
            
            return session;
            
        }else{
            log.warn("User id not found in user session table!");
        return null;
        }
    }
    
//    public HttpSession getUserSession(BigInteger usetId){
//        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);  
//    return null;
//    }
    
    
}
