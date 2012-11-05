/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.web.files;

import in.xfiles.core.entity.Files;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
/**
 *
 * @author 7
 */
@ManagedBean
@ViewScoped
public class FilesBean implements Serializable{

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
    
     public List<Files> getTableElements() {
         ArrayList<Files> list = new ArrayList<Files>();
         list.add(new Files(1L, "File1", "text", 1125468546L, 18626, false));
         list.add(new Files(2L, "File2", "text", 112546856L, 1259, false));
         
         return list;
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
         else return currentFile.getTypeId()+"";   
         
     }
}
