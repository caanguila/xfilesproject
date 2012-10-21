/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.ActionTypes;
import in.xfiles.core.entity.Logs;
import in.xfiles.core.entity.UserSession;
import in.xfiles.core.entity.User;
import java.util.Collection;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import javax.persistence.*;
import java.util.List;
/**
 *
 * @author 7
 */
@Stateless
@LocalBean
public class LogManager implements LogManagerLocal {
     private static final Logger log = Logger.getLogger(LogManager.class); 
      @PersistenceContext
      private EntityManager entityManager;
    
    @Override
    public Logs addRecord(Long userId, Long typeActionId, String message, String options) {
        User user = entityManager.find(User.class, userId);
        ActionTypes type = entityManager.find(ActionTypes.class, typeActionId);
        Logs logRecord = addRecord(user, type, message, options);
        
        return logRecord;
    }

    @Override
    public Logs addRecord(User user, ActionTypes type, String message, String options) {
        
        if(user instanceof User && type instanceof ActionTypes){
            UserSession session = getCurrentUserSession(user.getUserId());
            Logs logRecord = new Logs();
            logRecord.setIpAdress(session.getIpAdress());
            logRecord.setUserId(user);
            logRecord.setTypeActionId(type);
            logRecord.setSessionId(session);
            logRecord.setMessage(message);
            logRecord.setOptions(options);
            
            entityManager.persist(logRecord);
            log.info("Log Record added successfuly: "+logRecord);
            return logRecord;       
        }else{
            log.warn("User or ActionType is null");
        }
        
        return null;
    }

    @Override
    public Logs addRecord(User user, String typeName, String message, String options) {
     
        ActionTypes type = getActionTypeByName(typeName);
        Logs logRecord = addRecord(user, type, message, options);
        
        return logRecord;
    }

    @Override
    public Logs addRecord(Long userId, String typeName, String message, String options) {
       
        User user = entityManager.find(User.class, userId);
        ActionTypes type = getActionTypeByName(typeName);
        Logs logRecord = addRecord(user, type, message, options);
        
        return logRecord;
    }
    
    
    
    private ActionTypes getActionTypeByName(String typeName){
        Query query = entityManager.createQuery("select q from ActionTypes q where q.name =:typeName");
        query.setParameter("typeName", typeName);
        List<ActionTypes> list = query.getResultList();
        if(!list.isEmpty()){
            ActionTypes type = list.get(0);
            
            return type;
        }else{
            log.warn("Can't find ActionType by name:"+typeName);
        }
        
        return null;
    }
    
    private UserSession getCurrentUserSession(Long userId){
        User user = entityManager.find(User.class, userId);
        if(!(user instanceof User)){
            log.warn("Can't find user by userId: "+userId);
            return null;
        }
        Query query = entityManager.createQuery("select q from UserSession q where q.userId =:userId");
        query.setParameter("userId", user);
        List<UserSession> list = query.getResultList();
        if(!list.isEmpty()){
            UserSession session = list.get(0);
            return session;
        }else{
            log.warn("User id not found in user session table!");
        return null;
        }
        
    }

    @Override
    public Collection<ActionTypes> getAllActionTypes() {
        Query query = entityManager.createQuery("select q from ActionTypes q");
        return query.getResultList();
    }

    @Override
    public Collection<ActionTypes> getActionTypesByName(String name) {
        Query query = entityManager.createQuery("select q from ActionTypes q where q.name=:name");
        query.setParameter("name", name);
        
        return query.getResultList();
    }

    @Override
    public Collection<Logs> getRecordsByUser(User user) {
        Query query = entityManager.createQuery("select q from Logs q where q.userId=:name");
        query.setParameter("name", user);
        
        return query.getResultList();
    }
}
