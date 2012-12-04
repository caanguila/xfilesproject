package in.xfiles.web.upload;

import in.xfiles.core.ejb.UserManagerLocal;
import in.xfiles.core.entity.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author danon
 */
@ManagedBean
@ViewScoped
public class UserSelectionBean {
    
    @EJB
    private UserManagerLocal um;
            
    
    private String inputEmails;
    
    private List<User> parsedUsers = Collections.EMPTY_LIST;
    
    /**
     * Creates a new instance of UserSelectionBean
     */
    public UserSelectionBean() {
    }

    public String getInputEmails() {
        return inputEmails;
    }

    public void setInputEmails(String inputEmails) {
        this.inputEmails = inputEmails;
    }

    public List<User> getParsedUsers() {
        return parsedUsers;
    }
    
    public void parseUsersAction(ActionEvent evt) {
        parsedUsers = new ArrayList<User>();
        StringTokenizer st = new StringTokenizer(inputEmails, ";,/\\^\r\n\t ");
        while(st.hasMoreTokens()) {
            String token = st.nextToken();
            User u = um.getUserByLogin(token);
            if(u != null)
                parsedUsers.add(u);
        }
    }
    
    
}
