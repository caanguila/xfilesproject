package in.xfiles.core.ejb;

import java.math.BigInteger;
import javax.ejb.Local;

/**
 *
 * @author 7
 */
@Local
public interface PasswordManagerLocal {
     boolean checkUserPassword(Long userId, String password);
     boolean checkUserPassword(String login, String password);
}
