package in.xfiles.web.shit;

import in.xfiles.core.ejb.*;
import in.xfiles.core.entity.Log;
import in.xfiles.core.entity.User;
import in.xfiles.core.entity.UserSession;
import in.xfiles.core.helpers.EJBHelper;
import java.math.BigInteger;
import java.util.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author 7
 */
@ManagedBean
@RequestScoped
public class TestLoginBean {


private String login;
    private String password;
    private String password1;
    /**
     * Creates a new instance of TestLoginBean
     */
    public TestLoginBean() {
        
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
//        EJBHelper ejbHelper = EJBHelper.getInstance();
//        UserManagerLocal userManager = ejbHelper.getBean(UserManagerLocal.class);
//        SessionManagerLocal sessionManager = ejbHelper.getBean(SessionManagerLocal.class);
//        LogManagerLocal logManager =  ejbHelper.getBean(LogManagerLocal.class);
//       
//        
//        System.out.printf("Login: "+login+" password: "+password+" password1: "+password1);
//        User user = userManager.createUser(login, "Danshin", new Date(), null, "danon@frtk.ru", "I'm a proger", 1L);
//        
//        HttpServletRequest httpServletRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        UserSession session =  sessionManager.createUserSession(user.getUserId(), httpServletRequest.getRemoteAddr());
//        
//        logManager.addRecord(user.getUserId(), new Long("1"), "User created", "");
//        
//        Collection<Logs> rec = logManager.getRecordsByUser(user);
//        Iterator<Logs> iter = rec.iterator();
//        while(iter.hasNext()){
//            Log one = iter.next();
//        System.out.printf("User records: "+one.getMessage());
//        }
        
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
