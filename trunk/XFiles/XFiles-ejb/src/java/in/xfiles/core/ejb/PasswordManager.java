/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import java.math.BigInteger;
import javax.ejb.Stateless;

/**
 *
 * @author 7
 */
@Stateless
public class PasswordManager implements PasswordManagerLocal{
    
    public boolean checkUserPassword(BigInteger userId, String password){
    return false;
    }
    
}
