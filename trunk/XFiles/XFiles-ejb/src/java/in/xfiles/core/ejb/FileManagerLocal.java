package in.xfiles.core.ejb;

import in.xfiles.core.entity.DownloadRequest;
import in.xfiles.core.entity.Files;
import in.xfiles.core.wrappers.UploadedFileWrapper;
import javax.ejb.Local;
import java.util.Collection;
import java.util.List;
/**
 *
 * @author 7
 */
@Local
public interface FileManagerLocal {

    void processFile(UploadedFileWrapper ufw);
    
    void testDatabase(Long usertId);
    
    Collection<Files> getFilesByUser(Long usertId); 

    boolean fileExists(Long userId, long fileId);
    
    void requestDownload(long userId, long fileId, String key);

     Files getFileById(Long userId, long fileId);

     List<DownloadRequest> getRequestsByUserId(Long userId);

     DownloadRequest getRequestsById(Long reqiestId);
    
     void completeGroupFile(DownloadRequest request);
   
}
