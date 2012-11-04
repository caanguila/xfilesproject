package in.xfiles.web.users;

import in.xfiles.core.ejb.PasswordManagerLocal;
import in.xfiles.core.ejb.UserManagerLocal;
import in.xfiles.core.entity.User;
import in.xfiles.core.helpers.CryptoHelper;
import in.xfiles.core.helpers.StringUtils;
import in.xfiles.web.utils.JSFHelper;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * Session scoped managed bean for log in and log out.
 * You can also use it to check if current user is logged.
 *
 * @author danon
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
    
    private final Logger log = Logger.getLogger(RegistrationBean.class);
    
    @EJB
    private UserManagerLocal um;
    
    @EJB
    private PasswordManagerLocal pm;
    
    private String login;
    private String password;
    private String loginStatus;
    
    private User currentUser;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }
    
    public void preRenderView(ComponentSystemEvent evt) {
        if(!isLoggedIn()) {
            try {
                HttpServletResponse response = (HttpServletResponse)JSFHelper.getExternalContext().getResponse();
                if(!response.isCommitted()) {
                    response.sendRedirect(JSFHelper.getRequest().getContextPath()+"/faces/login.xhtml");
                }
            } catch (Exception ex) {
                if(log.isDebugEnabled()) {
                    log.debug("Failed to send redirect.", ex);
                }
            }
        }
    } 
    
    private boolean validateUserInput() {
        if(StringUtils.isEmpty(login))
            return false;
        if(StringUtils.isEmpty(password))
            return false;
        return true;
    }
    
    private boolean tryLogin(String login, String pwd) {
       Long userId = pm.checkUserPassword(login, pwd);
       if(userId == null)
           return false;
       currentUser = um.getUserById(userId);
       JSFHelper.setSessionAttribute("userId", userId);
       return currentUser != null;
    }
    
    public void loginAction(ActionEvent evt) {
        currentUser = null;
        
        if(!validateUserInput()) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Validation:", "Check your input and try again.");
            loginStatus = "fail";
            password = null;
            return;
        }
        
        String pwd = CryptoHelper.SHA256(password);
        password = null;
        
        if(!tryLogin(login, pwd)) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Authentication:", "Incorrect credentials.");
            loginStatus = "fail";
            pwd = null;
            return;
        }
        
        loginStatus = "ok";
    }
    
    public String logOut() {
        currentUser = null;
        JSFHelper.setSessionAttribute("userId", null);
        return "login.xhtml?faces-redirect=true";
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
