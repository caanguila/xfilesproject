/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.Types;
import in.xfiles.core.entity.Users;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

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
    
    public Users createUser(Map Params){
        Users user = null;
        //TO-DO Realisation
        return user;
    }
    
    public Users createUser(Set Params){
        Users user = null;
        //TO-DO Realisation
        return user;
    }
    
    public Users createUser(String name, String surname, Date dateCreation, Date dateSuspend, String email, String information, BigInteger type ){
        Long typeID = new Long(type.toString());
        Types otype = entityManager.find(Types.class, typeID);
        
        if(otype instanceof Types){
      
        Users user = new Users();
      //  user.setUserId(new Long("1001"));
        user.setName(name);
        user.setSurname(surname);
        user.setDateCreation(dateCreation);
        user.setDateSuspended(dateSuspend);
        user.setEmail(email);
        user.setInformation(information);
        user.setTypeId(otype);
        log.info("User: "+user);
       
       entityManager.persist(user);
  
        return user;
        }
        else{
         log.warn("Type id font find");   
            return null;
        }
        //TO-DO Realisation
        
    }
    
    public boolean removeUserById(BigInteger userId){
        //TO-DO Realisation
        return false;
    }
    
    public Users getUserById(BigInteger userId){
       Users user = null;
        //TO-DO Realisation
        return user; 
    }
    
}
