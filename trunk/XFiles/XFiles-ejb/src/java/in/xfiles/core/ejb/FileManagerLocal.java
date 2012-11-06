/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.Files;
import in.xfiles.core.wrappers.UploadedFileWrapper;
import javax.ejb.Local;
import java.util.Collection;
/**
 *
 * @author 7
 */
@Local
public interface FileManagerLocal {

    public void processFile(UploadedFileWrapper ufw);
    
    public void testDatabase(Long usertId);
    
    public Collection<Files> getFilesByUser(Long usertId); 
   
}
