/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.wrappers.UploadedFileWrapper;
import javax.ejb.Local;

/**
 *
 * @author 7
 */
@Local
public interface FileManagerLocal {

    public void processFile(UploadedFileWrapper ufw);
    
}
