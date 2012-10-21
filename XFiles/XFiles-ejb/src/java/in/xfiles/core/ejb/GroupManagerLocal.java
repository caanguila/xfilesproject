/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.Files;
import in.xfiles.core.entity.User;
import java.math.BigInteger;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author 7
 */
@Local
interface GroupManagerLocal {
     Collection<User> getUsers(BigInteger groupId);
    
     Collection<User> getUsersByType(BigInteger typeId);
    
     Collection<Files> getGroupFilesById(BigInteger groupId);
}
