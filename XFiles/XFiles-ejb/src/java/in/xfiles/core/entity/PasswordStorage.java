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
@Table(name = "password_storage")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PasswordStorage.findAll", query = "SELECT p FROM PasswordStorage p"),
    @NamedQuery(name = "PasswordStorage.findByPasswordStorageId", query = "SELECT p FROM PasswordStorage p WHERE p.passwordStorageId = :passwordStorageId"),
    @NamedQuery(name = "PasswordStorage.findByUserId", query = "SELECT p FROM PasswordStorage p WHERE p.userId = :userId"),
    @NamedQuery(name = "PasswordStorage.findByPassword", query = "SELECT p FROM PasswordStorage p WHERE p.password = :password"),
    @NamedQuery(name = "PasswordStorage.findByOptions", query = "SELECT p FROM PasswordStorage p WHERE p.options = :options")})
public class PasswordStorage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName="xfiles_seq", name="seq", allocationSize=1, initialValue=1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
    @Column(name = "password_storage_id", columnDefinition = "BIGSERIAL")
    private Long passwordStorageId;
   
    
    @Column(name = "user_id")
    private long userId;
    @Size(max = 500)
    @Column(name = "password")
    private String password;
    @Size(max = 500)
    @Column(name = "options")
    private String options;
    @ManyToMany(mappedBy = "passwordStorageCollection")
    private Collection<Files> filesCollection;
  
    
    
    public PasswordStorage() {
    }

    public PasswordStorage(Long passwordStorageId) {
        this.passwordStorageId = passwordStorageId;
    }

    public PasswordStorage(Long passwordStorageId, long userId) {
        this.passwordStorageId = passwordStorageId;
        this.userId = userId;
    }

    public Long getPasswordStorageId() {
        return passwordStorageId;
    }

    public void setPasswordStorageId(Long passwordStorageId) {
        this.passwordStorageId = passwordStorageId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    @XmlTransient
    public Collection<Files> getFilesCollection() {
        return filesCollection;
    }

    public void setFilesCollection(Collection<Files> filesCollection) {
        this.filesCollection = filesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (passwordStorageId != null ? passwordStorageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PasswordStorage)) {
            return false;
        }
        PasswordStorage other = (PasswordStorage) object;
        if ((this.passwordStorageId == null && other.passwordStorageId != null) || (this.passwordStorageId != null && !this.passwordStorageId.equals(other.passwordStorageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.xfiles.core.entity.PasswordStorage[ passwordStorageId=" + passwordStorageId + " ]";
    }
    
}
