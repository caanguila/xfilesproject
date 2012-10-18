/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import java.math.BigInteger;
import javax.ejb.Local;

/**
 *
 * @author 7
 */
@Local
public interface PasswordManagerLocal {
     boolean checkUserPassword(BigInteger userId, String password);
}
