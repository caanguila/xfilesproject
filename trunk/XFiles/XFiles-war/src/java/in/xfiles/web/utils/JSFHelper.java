package in.xfiles.web.utils;

import in.xfiles.core.ejb.UserManagerLocal;
import in.xfiles.core.entity.User;
import in.xfiles.core.helpers.EJBHelper;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Provides some handy routines for working with JSF context and session
 * @author danon
 */
public abstract class JSFHelper {
    
    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
    
    public static ExternalContext getExternalContext() {
        final FacesContext fc = getFacesContext();
        if(fc == null)
            return null;
        return fc.getExternalContext();
    }
    
    public static HttpServletRequest getRequest() {
        final ExternalContext ec = getExternalContext();
        if(ec == null)
            return null;
        return (HttpServletRequest)ec.getRequest();
    }
    
    public static Application getApplication() {
        final FacesContext fc = getFacesContext();
        if(fc == null) 
            return null;
        return fc.getApplication();
    }
    
    public static HttpSession getSession(boolean create) {
        final HttpServletRequest request = getRequest();
        if(request == null)
            return null;
        return request.getSession(create);
    }
    
    public static <T> T getSessionAttribute(Class<T> clazz, String name) {
        return SessionHelper.getSessionAttribute(clazz, getSession(false), name);
    }
    
    public static void setSessionAttribute(String name, Object value) {
        SessionHelper.setSessionAttribute(getSession(false), name, value);
    }
    
    public static User getCurrentUser() {
        Long userId = getSessionAttribute(Long.class, "user_id");
        if(userId == null) {
            return null;
        }
        UserManagerLocal um = EJBHelper.getInstance().getBean(UserManagerLocal.class);
        return um.getUserById(userId);
    }

    public static FacesMessage addMessage(FacesContext fc, FacesMessage.Severity severity, String summary, String details) {
        final FacesMessage msg = new FacesMessage(severity, summary, details);
        if(fc == null)
            return msg;
        fc.addMessage(null, msg);
        return msg;
    }
    
    public static FacesMessage addMessage(FacesMessage.Severity severity, String summary, String details) {
        return addMessage(getFacesContext(), severity, summary, details);
    }
}
