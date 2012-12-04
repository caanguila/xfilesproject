package in.xfiles.core.wrappers;

import in.xfiles.core.entity.User;
import java.io.File;
import java.io.Serializable;
import java.util.Set;

/**
 * Aggregates all necessary information about uploaded file.
 * 
 * @author danon
 */
public class UploadedFileWrapper implements Serializable {
    
    private File file;
    private String name;
    private String contentType;
    private String accessType;
    private String encryptionType;
    private long size;
    private long checksum;
    private User uploadedBy;
    private String key;
    private Set<User> groupUsers;

    public File getFile() {
        return file;
    }

    public void setFile(File f) {
        this.file = f;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getEncryptionType() {
        return encryptionType;
    }

    public void setEncryptionType(String encryptionType) {
        this.encryptionType = encryptionType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getChecksum() {
        return checksum;
    }

    public void setChecksum(long checksum) {
        this.checksum = checksum;
    }

    public User getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Set<User> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(Set<User> groupUsers) {
        this.groupUsers = groupUsers;
    }

    @Override
    public String toString() {
        return "UploadedFileWrapper{" + "file=" + file + ", name=" + name + ", contentType=" + contentType + ", accessType=" + accessType + ", encryptionType=" + encryptionType + ", size=" + size + ", checksum=" + checksum + ", uploadedBy=" + uploadedBy + '}';
    }
}
