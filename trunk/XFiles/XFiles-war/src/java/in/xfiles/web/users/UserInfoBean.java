package in.xfiles.web.users;

import in.xfiles.core.ejb.LogManagerLocal;
import in.xfiles.core.ejb.UserManagerLocal;
import in.xfiles.core.entity.Files;
import in.xfiles.core.entity.Logs;
import in.xfiles.core.entity.User;
import in.xfiles.web.utils.JSFHelper;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
    
    private final Logger log = Logger.getLogger(UserInfoBean.class);
    @EJB
    UserManagerLocal userManager;
    @EJB
    LogManagerLocal lm;
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
    
    public void saveParametersAction(ActionEvent e) {
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
    
    public List<Logs> getHistoryElements() {
        System.out.println("User Logs size: " + lm.getRecordsByUser(user));
        Collection<Logs> logs = lm.getRecordsByUser(user);
        ArrayList<Logs> list = new ArrayList<Logs>();
        // list.add(new Files(1L, "File1", "text", 1125468546L, 18626, false));
        if (logs == null) {
            return list;
        }
        Iterator<Logs> iter = logs.iterator();        
        while (iter.hasNext()) {
            Logs one = iter.next();
            list.add(one);
        }
        Collections.sort(list);
        
        
        historyPageCount = logs.size()/step;
        if(logs.size()%step >0) historyPageCount++;
        
        return list;
    }
    
    public List<Logs> getNextHistoryElements() {
        List<Logs> out = new ArrayList<Logs>();
        List<Logs> part = getHistoryElements();
        
        int length = part.size();
        if(currentHistoryIndex >= length) currentHistoryIndex -= step;
        
       // historyPage = currentHistoryIndex/step + 1;
       // if(historyPage>historyPageCount) historyPage = historyPageCount;
        
        for(int i = currentHistoryIndex; i<currentHistoryIndex+step && i<length; i++){
           // log.info(""+part.get(i));
        out.add(part.get(i));
        }
        
        
        return out;
    }
    
    public int getHistoryPagesCount(){
        return historyPageCount;
    }
    
    public void setStepAction(int i){
        step = i;
        log.info("Step: "+step);
        historyPageCount = getHistoryElements().size()/step;
        if(getHistoryElements().size()%step >0) historyPageCount++;
        currentHistoryIndex = 0;
        historyPage = 1;
    }
    
    public void nextAction(){
        currentHistoryIndex +=step;
        historyPage++;
        if(historyPage > historyPageCount) historyPage = historyPageCount;
        log.info("Next: "+currentHistoryIndex);
    } 
    
    public void previousAction(){
        currentHistoryIndex -=step;
        historyPage--;
        if(historyPage < 1) historyPage = 1;
        if(currentHistoryIndex<0) currentHistoryIndex = 0;
        log.info("Previous: "+currentHistoryIndex);
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
    
    
}