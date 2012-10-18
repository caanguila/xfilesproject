/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.Files;
import in.xfiles.core.entity.Users;
import java.math.BigInteger;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author 7
 */
@Local
interface GroupManagerLocal {
     Collection<Users> getUsers(BigInteger groupId);
    
     Collection<Users> getUsersByType(BigInteger typeId);
    
     Collection<Files> getGroupFilesById(BigInteger groupId);
}
