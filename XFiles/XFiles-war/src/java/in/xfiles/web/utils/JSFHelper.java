package in.xfiles.web.utils;

import in.xfiles.core.ejb.UserManagerLocal;
import in.xfiles.core.entity.User;
import in.xfiles.core.helpers.EJBHelper;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Provides some handy routines for working with JSF context and session.
 *
 * @author danon
 */
public class JSFHelper {
    
     private final FacesContext context;

    /**
     * Creates new instance with current Faces context.
     */
    public JSFHelper() {
        this(FacesContext.getCurrentInstance());
    }
    
    public JSFHelper(FacesContext context) {
        this.context = context;
    }
    
    public FacesContext getFacesContext() {
        return context;
    }

    public ExternalContext getExternalContext() {
        final FacesContext fc = getFacesContext();
        if (fc == null) {
            return null;
        }
        return fc.getExternalContext();
    }

    public HttpServletRequest getRequest() {
        final ExternalContext ec = getExternalContext();
        if (ec == null) {
            return null;
        }
        return (HttpServletRequest) ec.getRequest();
    }

    public Application getApplication() {
        final FacesContext fc = getFacesContext();
        if (fc == null) {
            return null;
        }
        return fc.getApplication();
    }

    public HttpSession getSession(boolean create) {
        final HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getSession(create);
    }
    
    public Long getUserId() {
        return getSessionAttribute(Long.class, "user_id");
    }

    public void setUserId(Long userId) {
        setSessionAttribute("user_id", userId);
    }

    public FacesMessage addMessage(FacesMessage.Severity severity, String summary, String details) {
        return JSFHelper.addMessage(getFacesContext(), null, severity, summary, details);
    }
    
    public FacesMessage addMessage(String component, FacesMessage.Severity severity, String summary, String details) {
        return JSFHelper.addMessage(getFacesContext(), component, severity, summary, details);
    }
    
    public void redirect(String nav) {
        NavigationHandler handler = getApplication().getNavigationHandler();
        handler.handleNavigation(getFacesContext(), null, nav+"?faces-redirect=true");
    }
    
    public static FacesMessage addMessage(FacesContext fc, String component, FacesMessage.Severity severity, String summary, String details) {
        return addMessage(fc, component, new FacesMessage(severity, summary, details));
    }
    
    public static FacesMessage addMessage(FacesContext fc, String component, FacesMessage msg) {
        if (fc != null) {
            fc.addMessage(component, msg);
        }
        return msg;
    }

    public <T> T getSessionAttribute(Class<T> clazz, String name) {
        return SessionUtils.getSessionAttribute(clazz, getSession(false), name);
    }

    public void setSessionAttribute(String name, Object value) {
        SessionUtils.setSessionAttribute(getSession(false), name, value);
    }

    public User getCurrentUser() {
        Long userId = getUserId();
        if (userId == null) {
            return null;
        }
        UserManagerLocal um = EJBHelper.getInstance().getBean(UserManagerLocal.class);
        return um.getUserById(userId);
    }
    
    public boolean validateQaptcha(boolean showMessages, String summary, String details) {
        String qaptchaKey = getSessionAttribute(String.class, "qaptcha_key");
        if(qaptchaKey == null) {
            if(showMessages)
                addMessage(FacesMessage.SEVERITY_ERROR, summary, details);
            return false;
        }
        
        if(!getRequest().getParameterMap().containsKey(qaptchaKey)) {
            if(showMessages)
                addMessage(FacesMessage.SEVERITY_ERROR, summary, details);
            return false;
        }
        return true;
    }
}
