package in.xfiles.web.controllers;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author 7
 */
@ManagedBean
@ViewScoped
public class CabinetController implements Serializable {

    public static String FILES_TAB = "files";
    public static String GROUPS_TAB = "groups";
    public static String REQUESTS_TAB = "requests";
    public static String MESSAGES_TAB = "messages";
    public static String PROFILE_TAB = "profile";
    public static String HISTORY_TAB = "history";
    
    
    private Map<String, Boolean> tabs;

    /**
     * Creates a new instance of CabinetController
     */
    public CabinetController() {
        tabs = Collections.synchronizedMap(new HashMap<String, Boolean>());
        flushFlags();
    }

    public void filesLinkListener(ActionEvent e) {
        flushFlags();
        tabs.put(FILES_TAB, true);

    }
    
    public void requestsLinkListener(ActionEvent e) {
        flushFlags();
        tabs.put(REQUESTS_TAB, true);
    }

    public void historyLinkListener(ActionEvent e) {
        flushFlags();
        tabs.put(HISTORY_TAB, true);
    }

    public void groupsLinkListener(ActionEvent e) {
        flushFlags();
        tabs.put(GROUPS_TAB, true);
    }

    public void messagesLinkListener(ActionEvent e) {
        flushFlags();
        tabs.put(MESSAGES_TAB, true);
    }

    public void profileLinkListener(ActionEvent e) {
        flushFlags();
        tabs.put(PROFILE_TAB, true);
    }

    public boolean isMessagesVisible() {
        return tabs.get(MESSAGES_TAB);
    }

    public void setMessagesVisible(boolean messagesVisible) {
        tabs.put(MESSAGES_TAB, messagesVisible);
    }

    public boolean isProfileVisible() {
        return tabs.get(PROFILE_TAB);
    }

    public void setProfileVisible(boolean profileVisible) {
        tabs.put(PROFILE_TAB, profileVisible);
    }

    public boolean isHistoryVisible() {
        return tabs.get(HISTORY_TAB);
    }

    public void setHistoryVisible(boolean historyVisible) {
        tabs.put(HISTORY_TAB, historyVisible);
    }

    public boolean isGroupsVisible() {
        return tabs.get(GROUPS_TAB);
    }

    public void setGroupsVisible(boolean groupsVisible) {
        tabs.put(GROUPS_TAB, groupsVisible);
    }

    private void flushFlags() {
        tabs.put(FILES_TAB, false);
        tabs.put(GROUPS_TAB, false);
        tabs.put(REQUESTS_TAB, false);
        tabs.put(MESSAGES_TAB, false);
        tabs.put(PROFILE_TAB, false);
        tabs.put(HISTORY_TAB, false);
    }

    public boolean renderUserFilesTable() {
        return true;
    }

    public void clickFile(ActionEvent e) {
        System.out.println("Click File");
    }

    public boolean isTableVisible() {
        return tabs.get(FILES_TAB);
    }

    public void setTableVisible(boolean tableVisible) {
        tabs.put(FILES_TAB,tableVisible);
    }

    public boolean isRequestsVisible() {
        return tabs.get(REQUESTS_TAB);
    }

    public void setRequestsVisible(boolean requestsVisible) {
        tabs.put(REQUESTS_TAB, requestsVisible);
    }
}
