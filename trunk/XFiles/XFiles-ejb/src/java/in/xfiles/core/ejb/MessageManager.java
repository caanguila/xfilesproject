/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.Groups;
import in.xfiles.core.entity.Messages;
import in.xfiles.core.entity.Types;
import in.xfiles.core.entity.User;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import javax.persistence.Query;
/**
 *
 * @author 7
 */
@Stateless
public class MessageManager implements MessageManagerLocal{
    private static final Logger log = Logger.getLogger(MessageManager.class);
    @PersistenceContext
    private EntityManager entityManager;
    
    public Collection<Messages> getUserInputMessages(Long userId){
        if(userId == null) return null;
       User user = entityManager.find(User.class, userId);
        if(user == null){
            log.warn("user: "+user);
        }
        
       return entityManager.createQuery("select u from Messages u where u.recipientId=:userId").setParameter("userId", user).getResultList();
              
    }
    
    public Collection<Messages> getMessagesByDate(Date creation, Date recieve){
        return null;
    }

    @Override
    public void sendMessage(Long recipientId, Long senderId, String message, Long typeId, Long groupId) {
        User recipient = entityManager.find(User.class, recipientId);
        if(recipient == null){
            log.warn("recipient user: "+recipient);
            return;
        }
        User sender = entityManager.find(User.class, senderId);
        if(sender == null){
            log.warn("sender user: "+sender);
            return;
        }
        Types type = entityManager.find(Types.class, typeId);
        if(type == null){
            log.warn("type is incorrect: "+type);
            return;
        }
        Groups gr =null;
        if(groupId!=null){
            gr = entityManager.find(Groups.class, groupId);
            if(gr == null){
                log.warn("Group is incorrect: "+gr);
                return;
            }
        }
        Messages m = new Messages();
        m.setMessage(message);
        m.setRecipientId(recipient);
        m.setSenderId(sender);
        m.setTypeId(type);
        m.setGroupId(groupId);
        m.setDateSend(new Date());
        entityManager.persist(m);
    }

    @Override
    public void sendGruopMessage(Long groupId, String message, Long typeId, Long senderId) {
        Groups gr =entityManager.find(Groups.class, groupId);
            if(gr == null){
                log.warn("Group is incorrect: "+gr);
                return;
            }
        Types type = entityManager.find(Types.class, typeId);
        if(type == null){
            log.warn("type is incorrect: "+type);
            return;
        }
        User sender = entityManager.find(User.class, senderId);
        if(sender == null){
            log.warn("sender user: "+sender);
            return;
        }
        
        for(User u : gr.getUsersCollection()){
           if(!sender.equals(u))
           sendMessage(u.getUserId(), senderId, message, typeId, groupId);
        }
    }
    
   
    
}
