package in.xfiles.web.users;

import in.xfiles.core.ejb.LogManagerLocal;
import in.xfiles.core.ejb.PasswordManagerLocal;
import in.xfiles.core.ejb.SessionManagerLocal;
import in.xfiles.core.ejb.UserManagerLocal;
import in.xfiles.core.entity.User;
import in.xfiles.core.helpers.CommonConstants;
import in.xfiles.core.helpers.CryptoHelper;
import in.xfiles.core.helpers.StringUtils;
import in.xfiles.web.utils.JSFHelper;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
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
    
    @EJB
    LogManagerLocal lm;
    
    @EJB
    SessionManagerLocal sm;
    
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
       
       HttpSession session =  JSFHelper.getSession(true); 
       HttpServletRequest httpServletRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
       Long userId = pm.checkUserPassword(login, pwd);
      // sm.modifySession(session, userId, httpServletRequest.getRemoteAddr() , "TO_DO", session.getId());
       lm.addRecord(userId, CommonConstants.USER_LOGIN, "try to login", ""+new java.util.Date(), session.getId());
       
       if(userId == null)
           return false;
       currentUser = um.getUserById(userId);
       
       sm.modifySession(session, userId, httpServletRequest.getRemoteAddr() , "TO_DO", session.getId());
       lm.addRecord(userId, CommonConstants.SUCCESS_LOGIN, "success", ""+new java.util.Date(), session.getId());
       JSFHelper.setUserId(userId);
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
        HttpSession session =  JSFHelper.getSession(true); 
        lm.addRecord(currentUser.getUserId(), CommonConstants.USER_LOGOUT, "LogOut", ""+new java.util.Date(), session.getId());
        currentUser = null;
        JSFHelper.setUserId(null);
        return "login.xhtml?faces-redirect=true";
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if(login == null)
            this.login = null;
        else
            this.login = login.trim();
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
