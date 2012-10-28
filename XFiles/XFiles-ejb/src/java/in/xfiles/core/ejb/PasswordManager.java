package in.xfiles.core.ejb;

import javax.ejb.Stateless;

/**
 *
 * @author 7
 */
@Stateless
public class PasswordManager implements PasswordManagerLocal{
    
    public boolean checkUserPassword(Long userId, String password){
        return false;
    }
    
    public boolean checkUserPassword(String login, String password) {
        return false;
    }
    
}
