/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.web.files;

import in.xfiles.core.ejb.FileManagerLocal;
import in.xfiles.core.entity.Files;
import in.xfiles.core.helpers.CommonConstants;
import in.xfiles.web.utils.JSFHelper;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import javax.ejb.EJB;
import java.util.*;
/**
 *
 * @author 7
 */
@ManagedBean
@ViewScoped
public class FilesBean implements Serializable{

    @EJB
    FileManagerLocal bean;
    
    public Files currentFile;
    public boolean treeStatus = true;
    /**
     * Creates a new instance of FilesBean
     */
    public FilesBean() {
                
    }
    
    public void processTreeNode(Files elem){
            currentFile = elem;
      
    System.out.println("Current file: "+currentFile);
    }
    
    public void refreshAction(){
       
        currentFile = null;
    }
    
    public String fileStyle(Files elem){
        if(elem.getTypeId().getTypeId().equals(CommonConstants.GROUP_FILE_TYPE))
            return "color: #08C;";
            
            
        return "";
    }
     public List<Files> getTableElements() {
         System.out.println("User Files size: "+bean.getFilesByUser(JSFHelper.getUserId()).size());
        Collection<Files> files =  bean.getFilesByUser(JSFHelper.getUserId());
         ArrayList<Files> list = new ArrayList<Files>();
        // list.add(new Files(1L, "File1", "text", 1125468546L, 18626, false));
         if(files == null) return list;
         Iterator<Files> iter = files.iterator(); 
         while(iter.hasNext()){
             Files one = iter.next();
             list.add(one);
         }
         
       //  list.add(new Files(2L, "File2", "text", 112546856L, 1259, false));
         
         return list;
     }
     
    public void addNewDefaultFile(){
        
        System.out.println("User Files size: "+bean.getFilesByUser(JSFHelper.getUserId()).size());
        bean.testDatabase(JSFHelper.getUserId());
        
    }

  
  
    
     
      
     public Files getCurrentFile(){
         System.out.println("ID");
         return currentFile;
     }
     public String getCurrentFileName(){
         if(currentFile == null) return null;
         else return currentFile.getName();   
         
     }
     
     public String getCurrentFileID(){
         if(currentFile == null) return null;
         else return currentFile.getFileId()+"";   
         
     }
     public String getCurrentFileContType(){
         if(currentFile == null) return null;
         else return currentFile.getContentType();   
         
     }
     public String getCurrentFileSize(){
         if(currentFile == null) return null;
         else return currentFile.getSize()+"";   
         
     }
     public String getCurrentFileCRC(){
         if(currentFile == null) return null;
         else return currentFile.getCrc()+"";   
         
     }
     public String getCurrentFileIsFolder(){
         if(currentFile == null) return null;
         else return currentFile.getIsfolder()+"";   
         
     }
     public String getCurrentFileType(){
         if(currentFile == null) return null;
         else return currentFile.getTypeId().getName()+"";   
         
     }
}
