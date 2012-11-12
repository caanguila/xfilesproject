package in.xfiles.core.ejb;

import java.math.BigInteger;
import javax.ejb.Local;

/**
 *
 * @author 7
 */
@Local
public interface PasswordManagerLocal {
     Long checkUserPassword(Long userId, String password);
     Long checkUserPassword(String login, String password);

     public boolean recoverPassword(Long userId);
}
