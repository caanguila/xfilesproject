package in.xfiles.core.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
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
    @NamedQuery(name = "PasswordStorage.findByUserId", query = "SELECT p FROM PasswordStorage p WHERE p.passwordStoragePK.userId = :userId"),
    @NamedQuery(name = "PasswordStorage.findByEncTypeDefId", query = "SELECT p FROM PasswordStorage p WHERE p.passwordStoragePK.encTypeDefId = :encTypeDefId"),
    @NamedQuery(name = "PasswordStorage.findByKey", query = "SELECT p FROM PasswordStorage p WHERE p.key = :key")})
public class PasswordStorage implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PasswordStoragePK passwordStoragePK;
    @Size(max = 500)
    @Column(name = "key")
    private String key;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "passwordStorage")
    private Collection<Files> filesCollection;

    public PasswordStorage() {
    }

    public PasswordStorage(PasswordStoragePK passwordStoragePK) {
        this.passwordStoragePK = passwordStoragePK;
    }

    public PasswordStorage(long userId, long encTypeDefId) {
        this.passwordStoragePK = new PasswordStoragePK(userId, encTypeDefId);
    }

    public PasswordStoragePK getPasswordStoragePK() {
        return passwordStoragePK;
    }

    public void setPasswordStoragePK(PasswordStoragePK passwordStoragePK) {
        this.passwordStoragePK = passwordStoragePK;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
        hash += (passwordStoragePK != null ? passwordStoragePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PasswordStorage)) {
            return false;
        }
        PasswordStorage other = (PasswordStorage) object;
        if ((this.passwordStoragePK == null && other.passwordStoragePK != null) || (this.passwordStoragePK != null && !this.passwordStoragePK.equals(other.passwordStoragePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PasswordStorage[ passwordStoragePK=" + passwordStoragePK + " ]";
    }
    
}
