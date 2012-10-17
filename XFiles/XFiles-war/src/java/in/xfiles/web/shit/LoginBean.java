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
@SessionScoped
public class LoginBean {

private String login;
    private String password;
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
    public String process(){
      
        
        System.out.println("Hello "+login);
        return "index.xhtml?faces-redirect=true";
    }
    
}
