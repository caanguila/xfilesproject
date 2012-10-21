/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.web.shit;

import in.xfiles.core.ejb.*;
import in.xfiles.core.ejb.UserManagerLocal;
import in.xfiles.core.entity.Logs;
import in.xfiles.core.entity.User;
import in.xfiles.core.entity.UserSession;
import in.xfiles.core.helpers.EJBHelper;
import java.math.BigInteger;
import java.util.Date;
import java.util.*;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author 7
 */
@ManagedBean
@RequestScoped
public class LoginBean {


private String login;
    private String password;
    private String password1;
    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
        
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

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }
    
    public String process(){
      
        
        System.out.println("Hello "+login);
        return "index.xhtml?faces-redirect=true";
    }
    
    public void confirm(){
        UserManagerLocal userManager = (UserManagerLocal) EJBHelper.getInstance().findBeanByName("java:global/XFiles/XFiles-ejb/UserManager!in.xfiles.core.ejb.UserManager");
        SessionManagerLocal sessionManager = (SessionManagerLocal) EJBHelper.getInstance().findBeanByName("java:global/XFiles/XFiles-ejb/SessionManager!in.xfiles.core.ejb.SessionManager");
        LogManagerLocal logManager = (LogManagerLocal) EJBHelper.getInstance().findBeanByName("java:global/XFiles/XFiles-ejb/LogManager!in.xfiles.core.ejb.LogManager");
        
        System.out.printf("Login: "+login+" password: "+password+" password1: "+password1);
        User user = userManager.createUser(login, "Danshin", new Date(), null, "danon@frtk.ru", "I'm a proger", new BigInteger("1"));
        
        HttpServletRequest httpServletRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        UserSession session =  sessionManager.createUserSession(user.getUserId(), httpServletRequest.getRemoteAddr());
        
        logManager.addRecord(user.getUserId(), new Long("1"), "User created", "");
        
        Collection<Logs> rec = logManager.getRecordsByUser(user);
        Iterator<Logs> iter = rec.iterator();
        while(iter.hasNext()){
            Logs one = iter.next();
        System.out.printf("User records: "+one.getMessage());
        }
        
    }
    public String validate(){
        if(password instanceof String && password1 instanceof String){
            if(password.equals(password1)) return "true";
        }
        
        return "false";
    }
    public boolean validatet(){
        if(password instanceof String && password1 instanceof String){
            if(password.equals(password1)) return true;
        }
        
        return false;
    }
    
}
