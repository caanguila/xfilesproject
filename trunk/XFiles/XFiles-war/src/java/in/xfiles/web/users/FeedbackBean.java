package in.xfiles.web.users;

import in.xfiles.core.ejb.EmailManagerLocal;
import in.xfiles.core.helpers.StringUtils;
import in.xfiles.web.BaseManagedBean;
import in.xfiles.web.utils.JSFHelper;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;

/**
 * JSF View scoped Managed bean for receiving users feedback.
 *
 * @author danon
 */
@ManagedBean
@ViewScoped
public class FeedbackBean extends BaseManagedBean {

    private static final Logger log = Logger.getLogger(FeedbackBean.class);
    
    private String message;
    private String name;
    private String email;
    
    @ManagedProperty(value="#{loginBean}")
    private LoginBean loginBean;
    
    @EJB
    private EmailManagerLocal em;
    
    /**
     * Creates a new instance of FeedbackBean
     */
    public FeedbackBean() {
       
    }
    
    public void sendFeedbackAction(ActionEvent evt) {
        JSFHelper helper = getJSFHelper();
        if(!helper.validateQaptcha(true, "Validation:", "Captcha validation failed!")) {
            return;
        }
        if(!StringUtils.isNotEmpty(message, name, email)) {
            helper.addMessage(FacesMessage.SEVERITY_ERROR, "Validation:", "All fields are mandatory.");
            return;
        }
        // TODO: regexp validate email
        try {
            em.sendSimpleEmail("Feedback from "+name+" "+email, message, "admin@xfiles.in");
            helper.addMessage(FacesMessage.SEVERITY_INFO, "Info:", "Your message has been sent.");
            message = null;
        } catch (Exception ex) {
            helper.addMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Unexpected server error.");
            log.error("sendFeedbackAction(): Failed to send user feedback message.", ex);
        }
    }
    
    @PostConstruct
    private void init() {
        if(loginBean == null || !loginBean.isLoggedIn())
            return;
        email = loginBean.getCurrentUser().getEmail();
        name = loginBean.getCurrentUser().getName() + " " + loginBean.getCurrentUser().getSurname();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }
}
