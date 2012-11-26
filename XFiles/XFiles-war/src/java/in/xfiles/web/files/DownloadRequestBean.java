package in.xfiles.web.files;

import in.xfiles.core.ejb.FileManagerLocal;
import in.xfiles.core.entity.DownloadRequest;
import in.xfiles.core.entity.Files;
import in.xfiles.core.helpers.CommonConstants;
import in.xfiles.core.helpers.CryptoHelper;
import in.xfiles.core.helpers.StringUtils;
import in.xfiles.web.utils.JSFHelper;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

/**
 * This bean provides some methods for creating and reviewing download requests
 * @author danon
 */
@ManagedBean
@ViewScoped
public class DownloadRequestBean implements Serializable {

    @EJB
    private FileManagerLocal fm;
    
    private String key;
    private String fileId;
    
    /**
     * Creates a new instance of DownloadRequestBean
     */
    public DownloadRequestBean() {
        fileId = StringUtils.getValidString(JSFHelper.getRequest().getParameter("file_id"));
        System.err.println("init!");
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    
    /**
     * Get all download requests made by current user
     * @return 
     */
    public List<DownloadRequest> getDownloadRequests() {
        return fm.getRequestsByUserId(JSFHelper.getUserId());
    }
    
    public void createRequest(ActionEvent evt) {
        System.err.println("hello");
        long fileId = StringUtils.getValidInt(this.fileId);
        fm.requestDownload(JSFHelper.getUserId(), fileId, CryptoHelper.SHA256(key));
        System.err.println("Download has been requested");
    }
    
    public boolean fileExists() {
        long fileId = StringUtils.getValidInt(this.fileId);
        return fm.fileExists(JSFHelper.getUserId(), fileId);
    }
    
    public Files getFile() {
        long fileId = StringUtils.getValidInt(this.fileId);
        return fm.getFileById(JSFHelper.getUserId(), fileId);
    }
    
    public boolean isPasswordNeeded() {
        Files f = getFile();
        if (f == null)
            return false;
        return f.getEncTypeId().getTypeId() != CommonConstants.PLAIN_ENCRYPTION_TYPE;
    }
}
