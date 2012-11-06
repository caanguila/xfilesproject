/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 7
 */
@Entity
@Table(name = "files")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Files.findAll", query = "SELECT f FROM Files f"),
    @NamedQuery(name = "Files.findByFileId", query = "SELECT f FROM Files f WHERE f.fileId = :fileId"),
    @NamedQuery(name = "Files.findByName", query = "SELECT f FROM Files f WHERE f.name = :name"),
    @NamedQuery(name = "Files.findByDescription", query = "SELECT f FROM Files f WHERE f.description = :description"),
    @NamedQuery(name = "Files.findByContentType", query = "SELECT f FROM Files f WHERE f.contentType = :contentType"),
    @NamedQuery(name = "Files.findByFileSize", query = "SELECT f FROM Files f WHERE f.fileSize = :fileSize"),
    @NamedQuery(name = "Files.findByCrc", query = "SELECT f FROM Files f WHERE f.crc = :crc"),
    @NamedQuery(name = "Files.findByIsfolder", query = "SELECT f FROM Files f WHERE f.isfolder = :isfolder")})
public class Files implements Serializable {
    private static final long serialVersionUID = 1L;
     @Id
    @SequenceGenerator(sequenceName="xfiles_seq", name="seq", allocationSize=1, initialValue=1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
    @Column(name = "file_id", columnDefinition = "BIGSERIAL")
    private Long fileId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "name")
    private String name;
    @Size(max = 1000)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "content_type")
    private String contentType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "file_size")
    private long fileSize;
    @Basic(optional = false)
    @NotNull
    @Column(name = "crc")
    private long crc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isfolder")
    private boolean isfolder;
    @JoinTable(name = "files_passwords", joinColumns = {
        @JoinColumn(name = "files_file_id", referencedColumnName = "file_id")}, inverseJoinColumns = {
        @JoinColumn(name = "password_storage_id", referencedColumnName = "password_storage_id")})
    @ManyToMany
    private Collection<PasswordStorage> passwordStorageCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentId")
    private Collection<Files> filesCollection;
    
    @JoinColumn(name = "parent_id", referencedColumnName = "file_id")
    @ManyToOne
    private Files parentId;

    @JoinColumn(name = "type_id", referencedColumnName = "type_id")
    @ManyToOne(optional = false)
    private Types typeId;

    @JoinColumn(name = "enc_type_id", referencedColumnName = "type_id")
    @ManyToOne(optional = false)
    private Types encTypeId;

    public Types getEncTypeId() {
        return encTypeId;
    }

    public void setEncTypeId(Types encTypeId) {
        this.encTypeId = encTypeId;
    }
    
    
    
    @JoinTable(name = "user_files", joinColumns = {
        @JoinColumn(name = "files_file_id", referencedColumnName = "file_id")}, inverseJoinColumns = {
        @JoinColumn(name = "users_user_id", referencedColumnName = "user_id")})
    @ManyToMany
    private Collection<User> usersCollection;
    
    @JoinTable(name = "files_groups", joinColumns = {
        @JoinColumn(name = "filesfile_id", referencedColumnName = "file_id")}, inverseJoinColumns = {
        @JoinColumn(name = "groupsgroup_id", referencedColumnName = "group_id")})
    @ManyToMany
    private Collection<Groups> groupsCollection;
    
    @JoinColumn(name = "created_by", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User createdBy;
    
    
    
    @JoinColumn(name = "en_file_id", referencedColumnName = "en_file_id")
    @ManyToOne(optional = false)
    private Encryptionfiles enFileId;

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Encryptionfiles getEnFileId() {
        return enFileId;
    }

    public void setEnFileId(Encryptionfiles enFileId) {
        this.enFileId = enFileId;
    }

    public Collection<Groups> getGroupsCollection() {
        return groupsCollection;
    }

    public void setGroupsCollection(Collection<Groups> groupsCollection) {
        this.groupsCollection = groupsCollection;
    }

    public Collection<User> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<User> usersCollection) {
        this.usersCollection = usersCollection;
    }
    
    
    
    public Types getTypeId() {
        return typeId;
    }

    public void setTypeId(Types typeId) {
        this.typeId = typeId;
    }
    
    
    public Files() {
    }

    public Files(Long fileId) {
        this.fileId = fileId;
    }

    public Files(Long fileId, String name, String contentType, long fileSize, long crc, boolean isfolder) {
        this.fileId = fileId;
        this.name = name;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.crc = crc;
        this.isfolder = isfolder;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return fileSize;
    }

    public void setSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getCrc() {
        return crc;
    }

    public void setCrc(long crc) {
        this.crc = crc;
    }

    public boolean getIsfolder() {
        return isfolder;
    }

    public void setIsfolder(boolean isfolder) {
        this.isfolder = isfolder;
    }

    @XmlTransient
    public Collection<PasswordStorage> getPasswordStorageCollection() {
        return passwordStorageCollection;
    }

    public void setPasswordStorageCollection(Collection<PasswordStorage> passwordStorageCollection) {
        this.passwordStorageCollection = passwordStorageCollection;
    }

    @XmlTransient
    public Collection<Files> getFilesCollection() {
        return filesCollection;
    }

    public void setFilesCollection(Collection<Files> filesCollection) {
        this.filesCollection = filesCollection;
    }

    public Files getParentId() {
        return parentId;
    }

    public void setParentId(Files parentId) {
        this.parentId = parentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fileId != null ? fileId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Files)) {
            return false;
        }
        Files other = (Files) object;
        if ((this.fileId == null && other.fileId != null) || (this.fileId != null && !this.fileId.equals(other.fileId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.xfiles.core.entity.Files[ fileId=" + fileId + " ]";
    }
    
}
