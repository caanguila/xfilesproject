package in.xfiles.core.ejb;

import in.xfiles.core.entity.User;
import in.xfiles.core.entity.UsersPasswords;
import in.xfiles.core.helpers.CommonTools;
import in.xfiles.core.helpers.CryptoHelper;
import in.xfiles.core.helpers.StringUtils;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 * Contains methods for checking user credentials when login.
 * 
 * @author 7
 */
@Stateless
public class PasswordManager implements PasswordManagerLocal{
    
    private static Logger log = Logger.getLogger(PasswordManager.class);
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    EmailManagerLocal emailMan;
    
    @EJB
    UserManagerLocal um;
    
    @Override
    public Long checkUserPassword(Long userId, String password){
        UsersPasswords up = CommonTools.getSingleElement(
                                em.createNamedQuery("UsersPasswords.findByUserIdAndPassword", UsersPasswords.class)
                                    .setParameter("userId", userId)
                                    .setParameter("password", password)
                                    .getResultList());
        if(log.isTraceEnabled()) {
            if(up == null)
                log.trace("checkUserPassword: incorrect credentials for userId = "+userId);
            else
                log.trace("checkUserPassword: access granted for userId = "+userId);
        }
        return up == null ? null : up.getUserId();
    }
    
    @Override
    public Long checkUserPassword(String login, String password) {
        UsersPasswords up = CommonTools.getSingleElement(
                                em.createNamedQuery("UsersPasswords.findByLoginAndPassword", UsersPasswords.class)
                                    .setParameter("login", login)
                                    .setParameter("password", password)
                                    .getResultList());
        if(log.isTraceEnabled()) {
            if(up == null)
                log.trace("checkUserPassword: incorrect credentials for login = "+login);
            else
                log.trace("checkUserPassword: access granted for login = "+login);
        }
        return up == null ? null : up.getUserId();
    }
    
    private <T> T getFirstElement(Collection<T> c) {
        if(c == null || c.isEmpty())
            return null;
        return c.iterator().next();
    } 

    @Override
    public boolean recoverPassword(Long userId) {
        System.err.println("recoverPassword(): userId = "+userId);
        if(userId == null)
            return false;
        UsersPasswords up = em.find(UsersPasswords.class, userId);
        User u = um.getUserById(userId);
        if(up == null)
            return false;
        try {
            String newPassword = generatePassword();
            emailMan.sendSimpleEmail("Password recovery", "Hello, "+u.getName()+" "+u.getSurname()
                    +".\nYour password has been reset. The new password is: "+newPassword
                    +"\n\nXFiles.IN", u.getEmail());
            up.setPassword(CryptoHelper.SHA256(newPassword));
            newPassword = null;
            em.persist(up);
            return true;
        } catch (Exception ex) {
            log.error("recoverPassword(): failed to recover password for: userId="+userId, ex);
        }
        return false;
    }

    private String generatePassword() {
        return StringUtils.generateRandomString(10);
    }
    
}
