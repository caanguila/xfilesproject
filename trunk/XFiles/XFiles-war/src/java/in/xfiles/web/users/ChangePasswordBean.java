package in.xfiles.web.users;

import in.xfiles.core.ejb.PasswordManagerLocal;
import in.xfiles.core.helpers.CryptoHelper;
import in.xfiles.core.helpers.StringUtils;
import in.xfiles.web.BaseManagedBean;
import in.xfiles.web.utils.JSFHelper;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

/**
 * This view scoped managed bean is used 
 * for changing password of current user.
 * 
 * @author danon
 */
@ManagedBean
@ViewScoped
public class ChangePasswordBean extends BaseManagedBean {

    @EJB
    private PasswordManagerLocal pm;
    
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    
    /**
     * Creates a new instance of ChangePasswordBean
     */
    public ChangePasswordBean() {
        
    }
    
    public void resetInput() {
        
    }
    
    private boolean validateUserInput() {
        JSFHelper helper = getJSFHelper();
        if(!StringUtils.isNotEmpty(oldPassword, newPassword, confirmPassword)) {
            helper.addMessage(FacesMessage.SEVERITY_ERROR, "Validation Error:", "All fields are required.");
            return false;
        }
        if(!confirmPassword.equals(newPassword)) {
            helper.addMessage(FacesMessage.SEVERITY_ERROR, "Validation Error:", "Confirmation password doesn't match new password.");
            return false;
        }
        
        // TODO: regexp check of password
        return true;
    }
    
    private boolean tryChangePassword() {
        JSFHelper helper = getJSFHelper();
        Long userId = helper.getUserId();
        if(userId == null) {
            helper.addMessage(FacesMessage.SEVERITY_ERROR, "Authentication Error:", "You should log in to change your password.");
            return false;
        }
        String oldPassword = CryptoHelper.SHA256(this.oldPassword);
        if(pm.checkUserPassword(userId, oldPassword) == null) {
            helper.addMessage(FacesMessage.SEVERITY_ERROR, "Password Validation Error:", "Invalid old password.");
            return false;
        }
        oldPassword = null;
        String newPassword = CryptoHelper.SHA256(this.newPassword);
        resetInput();
        try {
            pm.changeUserPassword(userId, newPassword);
        } catch (Exception ex) {
            helper.addMessage(FacesMessage.SEVERITY_ERROR, "Server Error:", "Unexpected server error occured.");
            return false;
        }
        return true;
    }
    
    public void changePasswordAction(ActionEvent evt) {
        if(!validateUserInput()) {
            resetInput();
            return;
        }
        if(!tryChangePassword()) {
            resetInput();
            return;
        }
        getJSFHelper().addMessage(FacesMessage.SEVERITY_INFO, "Info:", "Your password has been changed successfully.");
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
}
