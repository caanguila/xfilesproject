/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.Files;
import in.xfiles.core.entity.Groups;
import in.xfiles.core.entity.User;
import java.util.Collection;
import javax.ejb.Local;

@Local
public interface GroupManagerLocal {
     Collection<User> getUsers(Long groupId);
    
     Collection<User> getUsersByType(Long typeId);
    
     Collection<Files> getGroupFilesById(Long groupId);
     
     Collection<Groups> getGruopsByUser(Long userId);
     
     Groups createGroup(Collection<User> users, String name, String description, Long typeId);
}
