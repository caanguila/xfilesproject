package in.xfiles.web.users;

import in.xfiles.core.ejb.*;
import in.xfiles.core.entity.Log;
import in.xfiles.core.entity.User;
import in.xfiles.core.helpers.CommonConstants;
import in.xfiles.core.helpers.CryptoHelper;
import in.xfiles.core.helpers.StringUtils;
import in.xfiles.web.BaseManagedBean;
import in.xfiles.web.utils.JSFHelper;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
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
public class LoginBean extends BaseManagedBean {
    
    private final Logger log = Logger.getLogger(RegistrationBean.class);
    
    @EJB
    private UserManagerLocal um;
    
    @EJB
    private PasswordManagerLocal pm;
    
    @EJB
    private LogManagerLocal lm;
    
    @EJB
    private SessionManagerLocal sm;
    
    @EJB
    private SequreManagerLocal sml;
    
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
                JSFHelper helper = getJSFHelper();
                helper.redirect("/index");
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
       JSFHelper helper = getJSFHelper();
       HttpSession session =  helper.getSession(true); 
       HttpServletRequest httpServletRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      
       Long userId = pm.checkUserPassword(login, pwd);
       
      // sm.modifySession(session, userId, httpServletRequest.getRemoteAddr() , "TO_DO", session.getId());
       
       
       if(userId == null){
           User u = um.getUserByLogin(login);
           if(u!=null && u.getDateSuspended()!=null){
               currentUser = u;
               return false;
           }
           Log rec = lm.addRecord(userId, CommonConstants.USER_LOGIN, "try to login", "login="+login+":password="+pwd, session.getId());
           sml.validateUserInput(rec.getOptions());
           return false;
       }
       currentUser = um.getUserById(userId);
       if(currentUser.getDateSuspended() != null){
            return false;
       }
       sm.modifySession(session, userId, httpServletRequest.getRemoteAddr() , "TO_DO", session.getId());
       Log rec = lm.addRecord(userId, CommonConstants.SUCCESS_LOGIN, "success", "login="+login+":password="+pwd, session.getId());
       sml.validateUserInput(rec.getOptions());
       helper.setUserId(userId);
       return currentUser != null;
    }
    
    public void loginAction(ActionEvent evt) {
        JSFHelper helper = getJSFHelper();
        currentUser = null;
        if(!validateUserInput()) {
            helper.addMessage(FacesMessage.SEVERITY_ERROR, "Validation:", "Check your input and try again.");
            loginStatus = "fail";
            password = null;
            return;
        }
        
        String pwd = CryptoHelper.SHA256(password);
        password = null;
        
        if(!tryLogin(login, pwd)) {
            if(currentUser!=null && currentUser.getDateSuspended()!=null){
                helper.addMessage(FacesMessage.SEVERITY_ERROR, "Authentication:", "This user is banned since: "+currentUser.getDateSuspended());
                currentUser = null;
                loginStatus = "fail";
                pwd = null;
                return;
            }
            
            helper.addMessage(FacesMessage.SEVERITY_ERROR, "Authentication:", "Incorrect credentials.");
            loginStatus = "fail";
            pwd = null;
            return;
        }
        
        loginStatus = "ok";
    }
    
    public String logOut() {
        JSFHelper helper = getJSFHelper();
        HttpSession session =  helper.getSession(true); 
        lm.addRecord(currentUser.getUserId(), CommonConstants.USER_LOGOUT, "LogOut", "", session.getId());
        currentUser = null;
        helper.setUserId(null);
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
