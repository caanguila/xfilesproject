package in.xfiles.web.users;

import in.xfiles.core.ejb.*;
import in.xfiles.core.entity.*;
import in.xfiles.core.helpers.CommonConstants;
import in.xfiles.core.helpers.CryptoHelper;
import in.xfiles.core.helpers.ShamirSchema;
import in.xfiles.web.utils.JSFHelper;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;

/**
 *
 * @author 7
 */
@ManagedBean
@ViewScoped
public class UserInfoBean implements Serializable {

    private static final Logger log = Logger.getLogger(UserInfoBean.class);
    @EJB
    private UserManagerLocal userManager;
    @EJB
    private LogManagerLocal lm;
    @EJB
    private GroupManagerLocal gml;
    @EJB
    private SequreManagerLocal sml;
    @EJB
    private MessageManagerLocal mml;
    @EJB
    private PasswordManagerLocal pml;
    
    public Long userId;
    public String userName;
    public String surName;
    public Date dateCreation;
    public Date dateSuspend;
    public String email;
    public String information;
    private User user;
    public int step;
    public int currentHistoryIndex;
    public int historyPage;
    public int historyPageCount;
    public Groups currentGroup;
    public Messages currentMessage;
    
    public String password;
    /**
     * Creates a new instance of UserInfoBean
     */
    public UserInfoBean() {
        userId = JSFHelper.getUserId();
    }

    @PostConstruct
    private void init() {
        if (userId != null && user == null) {
            user = userManager.getUserById(userId);

        } else {
            log.warn("User Id is " + userId);
        }

        step = 10;
        currentHistoryIndex = 0;
        historyPage = 1;
    }

    
    public void refreshAction(){
     userId = JSFHelper.getUserId();   
     if (userId != null && user == null) {
            user = userManager.getUserById(userId);

        } else {
            log.warn("User Id is " + userId);
        }

        step = 10;
        currentHistoryIndex = 0;
        historyPage = 1;
        currentGroup = null;
        currentMessage = null;
    }
    
    
    
    public void saveParametersAction(ActionEvent evt) {
        log.info("Save parametr Action");
        log.info(userName);
        log.info(surName);
        log.info(dateCreation);
        log.info(dateSuspend);
        log.info(email);
        log.info(information);
        log.info(userId);
        user = userManager.modifyUserInfo(userId, userName, surName, information);


    }
    
    public List<Messages> getUserMessages(){
                if(userId == null) return null;
        return (List<Messages>) mml.getUserInputMessages(userId);
    }
    
    public List<Log> getHistoryElements() {
        if(userId == null) return null;
        System.out.println("User Logs size: " + lm.getRecordsByUser(user));
        Collection<Log> logs = lm.getRecordsByUser(user);
        ArrayList<Log> list = new ArrayList<Log>();
        // list.add(new Files(1L, "File1", "text", 1125468546L, 18626, false));
        if (logs == null) {
            return list;
        }
        Iterator<Log> iter = logs.iterator();
        while (iter.hasNext()) {
            Log one = iter.next();
            list.add(one);
        }
        Collections.sort(list);


        historyPageCount = logs.size() / step;
        if (logs.size() % step > 0) {
            historyPageCount++;
        }

        return list;
    }

    public List<Log> getNextHistoryElements() {
        if(userId == null) return null;
        List<Log> out = new ArrayList<Log>();
        List<Log> part = getHistoryElements();

        int length = part.size();
        if (currentHistoryIndex >= length) {
            currentHistoryIndex -= step;
        }

        // historyPage = currentHistoryIndex/step + 1;
        // if(historyPage>historyPageCount) historyPage = historyPageCount;

        for (int i = currentHistoryIndex; i < currentHistoryIndex + step && i < length; i++) {
            // log.info(""+part.get(i));
            out.add(part.get(i));
        }


        return out;
    }
    

    public void processGroupNode(Groups item) {
        currentGroup = item;
    }
     public void processMessageNode(Messages item) {
        currentMessage = item;
        log.debug("Message: "+currentMessage);
    }

    public boolean isGroupDownloadRequest(){
        if(currentMessage!= null){
            return currentMessage.getTypeId().getTypeId().equals(CommonConstants.GROUP_ACCESS_MESSAGE);
        }
        
        return false;
    }
    
    public void approveRequest(){
        //should modify record in requests table
        String pwd = CryptoHelper.SHA256(password);
        log.debug("user: "+user.getUserId()+" has accepted group file to user: "+currentMessage.getSenderId()+" password: "+pwd);
        Long uId = pml.checkUserPassword(userId, pwd);
        log.debug("uId: "+uId);
        if(uId!=null){
            boolean result = sml.approveRequestByUser(currentMessage);
            if(result)
                JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Request:", "Request has been approved");
            else
                JSFHelper.addMessage(FacesMessage.SEVERITY_WARN, "Request:", "Request has already been approved");
        }
        else{
        JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Request:", "Incorrect password, request has't been approved");
        }
        if(((List<Messages>) mml.getUserInputMessages(userId)).isEmpty()) currentMessage = null;
    }
    public void denyRequest(){
        //Should delete record from messages after deny/accept button
        log.debug("user: "+user.getUserId()+" has denied access group file to user: "+currentMessage.getSenderId());
    }


    public List<Groups> getUserGroups() {
        if(userId == null) return null;
        List<Groups> out = new ArrayList<Groups>();
        Collection<Groups> groups = gml.getGruopsByUser(userId);
        if (user != null) {
            for (Groups gr : groups) {
                out.add(gr);
            }
        }
        return out;
    }

    public List<User> getGroupsUsers() {
        if(userId == null) return null;
        List<User> out = new ArrayList<User>();
        if (currentGroup != null) {
            for (User u : currentGroup.getUsersCollection()) {
                out.add(u);
            }
            log.info("Group files: " + currentGroup.getFilesCollection().size());
        }
        return out;
    }

    public int getHistoryPagesCount() {
       
        return historyPageCount;
    }

    public void setStepAction(int i) {
        step = i;
        log.info("Step: " + step);
        historyPageCount = getHistoryElements().size() / step;
        if (getHistoryElements().size() % step > 0) {
            historyPageCount++;
        }
        currentHistoryIndex = 0;
        historyPage = 1;
    }

    public void nextAction() {
        currentHistoryIndex += step;
        historyPage++;
        if (historyPage > historyPageCount) {
            historyPage = historyPageCount;
        }
        log.info("Next: " + currentHistoryIndex);
    }

    public void previousAction() {
        currentHistoryIndex -= step;
        historyPage--;
        if (historyPage < 1) {
            historyPage = 1;
        }
        if (currentHistoryIndex < 0) {
            currentHistoryIndex = 0;
        }
        log.info("Previous: " + currentHistoryIndex);
    }

    
    
    public Messages getCurrentMessage() {
        return currentMessage;
    }

    public void setCurrentMessage(Messages currentMessage) {
        this.currentMessage = currentMessage;
    }

    
   
    public Groups getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(Groups currentGroup) {
        this.currentGroup = currentGroup;
    }
    
    public Date getDateCreation() {
        dateCreation = user.getDateCreation();
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateSuspend() {
        dateSuspend = user.getDateSuspended();
        return dateSuspend;
    }

    public void setDateSuspend(Date dateSuspend) {
        this.dateSuspend = dateSuspend;
    }

    public String getEmail() {
        email = user.getEmail();
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHistoryPage() {
        return historyPage;
    }

    public void setHistoryPage(int historyPage) {
        this.historyPage = historyPage;
    }

    public String getInformation() {
        information = user.getInformation();
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getSurName() {
        surName = user.getSurname();
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getUserName() {
        userName = user.getName();
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getCurrentHistoryIndex() {
        return currentHistoryIndex;
    }

    public void setCurrentHistoryIndex(int currentHistoryIndex) {
        this.currentHistoryIndex = currentHistoryIndex;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
   
}
