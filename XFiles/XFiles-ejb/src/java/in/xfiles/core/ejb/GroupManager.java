/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.*;
import in.xfiles.core.helpers.CommonConstants;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

@Stateless
public class GroupManager implements GroupManagerLocal {

    private static final Logger log = Logger.getLogger(GroupManager.class);
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private LogManagerLocal logManager;
    @EJB
    private SessionManagerLocal sessionManager;

    public Collection<User> getUsers(Long groupId) {
        Groups group = entityManager.find(Groups.class, groupId);
        if (group != null) {
            entityManager.getEntityManagerFactory().getCache().evictAll();
            return group.getUsersCollection();
        } else {
            log.warn("Group with id: " + groupId + " does not exist");
            return null;
        }
    }
    
    public Collection<Groups> getGruopsByUser(Long userId) {
        if (userId == null) {
            return Collections.EMPTY_LIST;
        }
        User user = entityManager.find(User.class, userId);

        if (user != null) {
            entityManager.getEntityManagerFactory().getCache().evictAll();
            return user.getGroupsCollection();
        } else {
            return null;
        }
    }

    public Collection<User> getUsersByType(Long typeId) {
        log.warn("Not supported yet");
        return null;
    }

    public Collection<Files> getGroupFilesById(Long groupId) {
        log.warn("Not supported yet");
        return null;
    }

    @Override
    public Groups createGroup(Collection<User> users, String name, String description, Long typeId) {
        Types type =  entityManager.find(Types.class, typeId);
        if(users.isEmpty()) {
            log.warn("Users collection should not be empty");
            return null;
        }
        if(type != null){
            Groups group = new Groups();
            group.setName(name);
            group.setDescription(description);
            group.setTypeId(type);
            group.setUsersCollection(users);
            entityManager.persist(group);
            entityManager.getEntityManagerFactory().getCache().evictAll();
            ActionTypes action =  entityManager.find(ActionTypes.class, CommonConstants.GROUP_CREATION);
        for(User u: users){
            UserSession us = sessionManager.getUserSession(u.getUserId());
            if(us!=null){
                logManager.addRecord(u.getUserId(), action.getActionTypeId(), "group created"+group.getName(), "", us.getSession());
            }else{
                logManager.addRecord(u.getUserId(), action.getActionTypeId(), "group created"+group.getName(), "", "");   
            }
        }
        
             return group;
        }else{
            log.warn("Can't find type " + typeId);
            return null;
        }
    }
}
