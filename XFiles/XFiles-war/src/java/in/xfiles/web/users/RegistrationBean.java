package in.xfiles.web.users;

import in.xfiles.core.ejb.LogManagerLocal;
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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * A simple managed bean for user registration
 * 
 * @author danon
 */
@ManagedBean
@ViewScoped
public class RegistrationBean implements CommonConstants, Serializable {
    
    private final Logger log = Logger.getLogger(RegistrationBean.class);
    
    @EJB
    private UserManagerLocal um;
    
    @EJB
    private LogManagerLocal lm;
    
    @EJB
    private SessionManagerLocal sm;
    
    private String firstName;
    private String lastName;
    private String password;
    private String passwordConfirmation;
    private String email;
    private String info;
    private String registrationStatus;

    /**
     * Creates a new instance of RegistrationBean
     */
    public RegistrationBean() {
    }
    
    private boolean validateUserInput() {
        if(StringUtils.isEmpty(email)) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Validation Error:", "E-mail field cannot be empty.");
            return false;
        }
        if(StringUtils.isEmpty(password)) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Validation Error:", "Password cannot be empty");
            return false;
        }
        if(!password.equals(passwordConfirmation)) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Validation Error:", "Password and Confirmation password don't match.");
            passwordConfirmation = null;
            password = null;
            return false;
        }
        if(StringUtils.isEmpty(firstName)) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Validation Error:", "Please, specify your first name.");
            return false;
        }
        if(StringUtils.isEmpty(lastName)) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Validation Error:", "Please, specify your last name.");
            return false;
        }
        return true;
    }
    
    public void registerAction(ActionEvent evt) {
        if(!validateUserInput()) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_WARN, 
                    "Information:", 
                    "Cannot register new user. Please, check your input and try again.");
            registrationStatus = "fail";
            return;
        }
        String pwd = CryptoHelper.SHA256(password);
        password = null; passwordConfirmation = null;
        log.info("password = "+pwd);
        log.info("e-mail = "+email);
        
        // Check if user exists
        if(um.getUserByLogin(email) != null) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_WARN, 
                    "Information:", 
                    "This login is taken by existing account.");
            registrationStatus = "fail";
            return;
        }
        
        User u = um.createUser(ADMINISTRATOR_USER_TYPE, email, firstName, lastName, pwd, info);
        pwd = null;
        if(u == null) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error:", 
                    "Unexpected error. See logs for details");
            registrationStatus = "fail";
            return;
        }
        
        
        
        if(u == null) {
            registrationStatus = "fail";
        } else {
            registrationStatus = "ok";
            HttpSession session =  JSFHelper.getSession(true); 
            HttpServletRequest httpServletRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            lm.addRecord(u.getUserId(), CommonConstants.REGESTRATION_OF_USER, "user registered", ""+new java.util.Date(), session.getId());
            //sm.modifySession(session, u.getUserId(), httpServletRequest.getRemoteAddr(), "TO_DO", session.getId());
            log.info("User has been registered: "+u);
        }
        
        
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email == null)
            this.email = null;
        else this.email = email.trim();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

}
