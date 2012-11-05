package in.xfiles.core.ejb;

import in.xfiles.core.wrappers.UploadedFileWrapper;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import org.apache.log4j.Logger;

/**
 *
 * @author 7
 */
@Stateless
public class FileManager implements FileManagerLocal{

    private Logger log = Logger.getLogger(FileManager.class);
    
    @Override @Asynchronous
    public void processFile(UploadedFileWrapper ufw) {
        log.debug("Started asynchronous processing of uploaded file: "+ufw);
        // TODO: implementation
        
        // Delete temporary file after processing
        ufw.getFile().delete();
    }
    
}
