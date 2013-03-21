package in.xfiles.web.users;

import in.xfiles.core.ejb.PasswordManagerLocal;
import in.xfiles.core.ejb.UserManagerLocal;
import in.xfiles.core.entity.User;
import in.xfiles.core.helpers.StringUtils;
import in.xfiles.web.BaseManagedBean;
import in.xfiles.web.utils.JSFHelper;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;

/**
 * View scoped managed bean for password recovery
 *
 * @author danon
 */
@ManagedBean
@ViewScoped
public class PasswordRecoveryBean extends BaseManagedBean {
    
    private final Logger log = Logger.getLogger(RegistrationBean.class);
    
    @EJB
    private UserManagerLocal um;
    
    @EJB
    private PasswordManagerLocal pm;
    
    private String login;
    private String recoveryStatus;

    /**
     * Creates a new instance of LoginBean
     */
    public PasswordRecoveryBean() {
    }
    
    private boolean validateUserInput() {
        if(StringUtils.isEmpty(login))
            return false;
        // TODO: validate login (regexp)
        return true;
    }
    
    private boolean tryRecover(String login) {
       User u = um.getUserByLogin(login);
       if(u == null)
           return false;
       return pm.recoverPassword(u.getUserId());
    }
    
    public void recoverAction(ActionEvent evt) {
        JSFHelper helper = getJSFHelper();
        if(!helper.validateQaptcha(true, "Validation:", "Captcha validation failed.")) {
            recoveryStatus = "fail";
            return;
        }
            
        if(!validateUserInput()) {
            helper.addMessage(FacesMessage.SEVERITY_ERROR, "Validation:", "Check your input and try again.");
            recoveryStatus = "fail";
            return;
        }
        
        if(!tryRecover(login)) {
            helper.addMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Password cannot be recovered.");
            recoveryStatus = "fail";
            return;
        }
        
        helper.addMessage(FacesMessage.SEVERITY_INFO, "Information:", "New password has been sent to your e-mail.");    
        recoveryStatus = "ok";
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

    public String getRecoveryStatus() {
        return recoveryStatus;
    }

    public void setRecoveryStatus(String recoveryStatus) {
        this.recoveryStatus = recoveryStatus;
    }
}
