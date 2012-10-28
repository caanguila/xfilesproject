package in.xfiles.web.utils;

import in.xfiles.core.ejb.UserManagerLocal;
import in.xfiles.core.entity.User;
import in.xfiles.core.helpers.CryptoHelper;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author danon
 */
@ManagedBean
@SessionScoped
public class UserSessionBean {
    private final Logger log = Logger.getLogger(UserSessionBean.class);
    
    private final Object loginLock = new Object();
    
    private User user;
    
    @EJB
    private UserManagerLocal um;
    
    private boolean signedIn = false;
    
    /**
     * Creates a new instance of UserSessionBean
     */
    public UserSessionBean() {
    }
    
    public void singIn(String login, String password) {
        String pwd = CryptoHelper.SHA256(password);
        password = null;
        
        final Long id = um.tryLogin(login, pwd);
        synchronized(loginLock) {
            // TODO: update user sessions!
            if(id == null) {
                JSFHelper.setSessionAttribute("user_id", null);
                signedIn = false;
                log.trace("Incorrect credentials");
                return;
            }

            JSFHelper.setSessionAttribute("user_id", id);
            signedIn = true;
            user = um.getUserById(id);
        }
    }

    public boolean isSignedIn() {
        return signedIn;
    }
    
    public void signOut() {
        synchronized(loginLock) {
            // TODO: update user sessions!
            JSFHelper.setSessionAttribute("user_id", null);
            signedIn = false;
            user = null;
        }
        log.debug("Logout successful");
    }
}
