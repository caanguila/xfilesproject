/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.managers;

import in.xfiles.core.entity.Users;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author 7
 */
public class UserManager {
    
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
        Users user = null;
        //TO-DO Realisation
        return user;
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
