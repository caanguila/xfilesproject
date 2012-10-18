/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.Users;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import javax.ejb.Local;

/**
 *
 * @author 7
 */
@Local
public interface UserManagerLocal {
    Users createUser(Map Params);

    Users createUser(Set Params);

    Users createUser(String name, String surname, Date dateCreation, Date dateSuspend, String email, String information, BigInteger type);

    boolean removeUserById(BigInteger userId);

    Users getUserById(BigInteger userId);

}
