package in.xfiles.web.files;

import in.xfiles.core.ejb.FileManagerLocal;
import in.xfiles.core.entity.DownloadRequest;
import in.xfiles.core.entity.Files;
import in.xfiles.core.helpers.CommonConstants;
import in.xfiles.core.helpers.CryptoHelper;
import in.xfiles.core.helpers.StringUtils;
import in.xfiles.web.utils.JSFHelper;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
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
    
    // initialized in constructor
    private String key;
    private String fileId;
    
    // initialized in PostConstruct method
    private boolean canDownload = false;
    private boolean fileExists;
    private Files file;
    
    /**
     * Creates a new instance of DownloadRequestBean
     */
    public DownloadRequestBean() {
        fileId = StringUtils.getValidString(JSFHelper.getRequest().getParameter("file_id"));
        key = null;
    }
    
    @PostConstruct
    private void init() {
        long fileId = StringUtils.getValidInt(this.fileId);
        fileExists = fm.fileExists(fileId);
        if(fileExists) {
            canDownload = fm.canDownload(JSFHelper.getUserId(), fileId);
            if(canDownload) {
                file = fm.getFileById(JSFHelper.getUserId(), fileId);
            }
        }
        
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

    public boolean isCanDownload() {
        return canDownload;
    }

    public boolean isFileExists() {
        return fileExists;
    }
    
    /**
     * Get all download requests made by current user
     * @return 
     */
    public List<DownloadRequest> getDownloadRequests() {
        return fm.getRequestsByUserId(JSFHelper.getUserId());
    }
    
    public void createRequest(ActionEvent evt) {
        long fileId = StringUtils.getValidInt(this.fileId);
        if(isPasswordNeeded())
            fm.requestDownload(JSFHelper.getUserId(), fileId, CryptoHelper.SHA256(key));
        else 
            fm.requestDownload(JSFHelper.getUserId(), fileId, null);
    }
    
    public String getStatusAsString(int status){
        switch (status){
            case DownloadRequest.APPROVED_STATUS: return "Approved";
            case DownloadRequest.ERROR_STATUS: return "Error";
            case DownloadRequest.EXPIRED_STATUS: return "Expired";
            case DownloadRequest.READY_STATUS: return "Ready";
            case DownloadRequest.REQUESTED_STATUS: return "Requested";
        }
        return "";
    }
    
    public Files getFile() {
        return file;
    }
    
    public boolean isPasswordNeeded() {
        Files f = getFile();
        if (f == null)
            return false;
      
        Long type = f.getEncTypeId().getTypeId();
        return type != CommonConstants.PLAIN_ENCRYPTION_TYPE 
                    && f.getTypeId().getTypeId() != CommonConstants.GROUP_FILE_TYPE;
    }
}
