/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.Files;
import in.xfiles.core.entity.Users;
import java.math.BigInteger;
import java.util.Collection;
import javax.ejb.Stateless;

/**
 *
 * @author 7
 */
@Stateless
public class GroupManager implements GroupManagerLocal {
    
    public Collection<Users> getUsers(BigInteger groupId){
        return null;
    }
    
    public Collection<Users> getUsersByType(BigInteger typeId){
        return null;
    }
    
    public Collection<Files> getGroupFilesById(BigInteger groupId){
     return null;  
    }
}