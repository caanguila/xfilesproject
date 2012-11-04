package in.xfiles.core.ejb;

import in.xfiles.core.entity.User;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author 7
 */
@Local
public interface UserManagerLocal {
    User createUser(Map<String, Object> params);

    User createUser(Long typeID, String email, String name, String surname, String password, String information);

    boolean removeUserById(Long userId);

    User getUserById(Long userId);

    public Long tryLogin(String login, String pwd);
    
    public User getUserByLogin(String login);

}
