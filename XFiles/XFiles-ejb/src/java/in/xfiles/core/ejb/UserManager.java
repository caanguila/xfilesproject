/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.Types;
import in.xfiles.core.entity.User;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.*;
import org.apache.log4j.Logger;
import java.util.List;
/**
 *
 * @author 7
 */
@Stateless
@LocalBean
public class UserManager implements UserManagerLocal{
    @PersistenceContext
    private EntityManager entityManager;
    
     private static final Logger log = Logger.getLogger(UserManager.class);
    
    public User createUser(Map<String, Object> Params){
        User user = null;
        //TO-DO Realisation
        return user;
    }
    
       
    public User createUser(String name, String surname, Date dateCreation, Date dateSuspend, String email, String information, BigInteger type ){
        Long typeID = new Long(type.toString());
        Types otype = entityManager.find(Types.class, typeID);
        
        if(otype instanceof Types){
      
        User user = new User();
      //  user.setUserId(new Long("1001"));
        user.setName(name);
        user.setSurname(surname);
        user.setDateCreation(dateCreation);
        user.setDateSuspended(dateSuspend);
        user.setEmail(email);
        user.setInformation(information);
        user.setTypeId(otype);
        
       
       entityManager.persist(user);
       log.info("User: "+user);
       
        return user;
        }
        else{
         log.warn("Type id font find");   
            return null;
        }
        //TO-DO Realisation
        
    }
    
    public boolean removeUserById(Long userId){
        //TO-DO Realisation
        return false;
    }
    
    public User getUserById(Long userId){
       User user = null;
        //TO-DO Realisation
        return user; 
    }
//    private User findUserByDate(Date dateCreation){
//        Query query = entityManager.createQuery("select q from User q where q.dateCreation=:date");
//        query.setParameter("date", dateCreation);
//        List<User> list = query.getResultList();
//        if(!list.isEmpty()){
//            User user = list.get(0);
//            log.debug("User found: "+user);
//            return user;
//        }else{
//            log.warn("Can't find user by date: "+dateCreation);
//        }
//        return null;
//    }
    
    
    
}
