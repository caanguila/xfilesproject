/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.web.shit;

import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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
        
        System.out.printf("Login: "+login+" password: "+password+" password1: "+password1);
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
