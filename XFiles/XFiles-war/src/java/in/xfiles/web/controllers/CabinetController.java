/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.web.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
/**
 *
 * @author 7
 */
@ManagedBean
@ViewScoped
public class CabinetController implements Serializable {
public boolean tableVisible = true;
public boolean groupsVisible;
public boolean messagesVisible;
public boolean profileVisible;
    /**
     * Creates a new instance of CabinetController
     */
    public CabinetController() {
    }
    
    public void filesLinkListener(ActionEvent e){
        
         
         flushFlags();
         System.out.println("Click Files:");
         if(tableVisible) tableVisible = false;
         else tableVisible = true;
         System.out.println("Visible: "+tableVisible);
         
    }
    
     public void groupsLinkListener(ActionEvent e){
         
         flushFlags();
         System.out.println("Click Groups:");
         if(groupsVisible) groupsVisible = false;
         else groupsVisible = true;
         System.out.println("Visible: "+groupsVisible);
         
    }
     
     public void messagesLinkListener(ActionEvent e){
         
         flushFlags();
         System.out.println("Click Messages:");
         if(messagesVisible) messagesVisible = false;
         else messagesVisible = true;
         System.out.println("Visible: "+messagesVisible);
         
    }

      public void profileLinkListener(ActionEvent e){
         
         flushFlags();
         System.out.println("Click Profile:");
         if(profileVisible) profileVisible = false;
         else profileVisible = true;
         System.out.println("Visible: "+profileVisible);
         
    }

    public boolean isMessagesVisible() {
        return messagesVisible;
    }

    public void setMessagesVisible(boolean messagesVisible) {
        this.messagesVisible = messagesVisible;
    }

    public boolean isProfileVisible() {
        return profileVisible;
    }

    public void setProfileVisible(boolean profileVisible) {
        this.profileVisible = profileVisible;
    }
      
      
      
    public boolean isGroupsVisible() {
        return groupsVisible;
    }

    public void setGroupsVisible(boolean groupsVisible) {
        this.groupsVisible = groupsVisible;
    }
    
    private void flushFlags(){
        groupsVisible = false;
        tableVisible = false;
        messagesVisible = false;
        profileVisible = false;
    }
     
    
    public boolean renderUserFilesTable(){
        return true;
    }

    public void clickFile(ActionEvent e){
         System.out.println("Click File");
    }
    
    public boolean isTableVisible() {
        return tableVisible;
    }

    public void setTableVisible(boolean tableVisible) {
        this.tableVisible = tableVisible;
    }
    
}
