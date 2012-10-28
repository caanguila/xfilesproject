package in.xfiles.web.users;

import in.xfiles.core.ejb.PasswordManagerLocal;
import in.xfiles.core.ejb.UserManagerLocal;
import in.xfiles.core.helpers.CryptoHelper;
import in.xfiles.core.helpers.StringUtils;
import in.xfiles.web.utils.JSFHelper;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;

/**
 *
 * @author danon
 */
@ManagedBean
@RequestScoped
public class LoginBean {
    
    private final Logger log = Logger.getLogger(RegistrationBean.class);
    
    @EJB
    private UserManagerLocal um;
    
    @EJB
    private PasswordManagerLocal pm;
    
    private String login;
    private String password;
    private String loginStatus;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }
    
    private boolean validateUserInput() {
        if(StringUtils.isEmpty(login))
            return false;
        if(StringUtils.isEmpty(password))
            return false;
        return true;
    }
    
    private boolean tryLogin(String login, String pwd) {
        return pm.checkUserPassword(login, pwd);
    }
    
    public void loginAction(ActionEvent evt) {
        if(!validateUserInput()) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Validation Error:", "Check your input and try again.");
            loginStatus = "fail";
            password = null;
            return;
        }
        
        String pwd = CryptoHelper.SHA256(password);
        password = null;
        
        if(!tryLogin(login, pwd)) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Authentication Error:", "Incorrect credentials.");
            loginStatus = "fail";
            pwd = null;
            return;
        }
        
        loginStatus = "ok";
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
}
