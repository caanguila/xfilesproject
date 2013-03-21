package in.xfiles.core.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 7
 */
@Entity
@Table(name = "encryptionfiles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Encryptionfiles.findAll", query = "SELECT e FROM Encryptionfiles e"),
    @NamedQuery(name = "Encryptionfiles.findByEnFileId", query = "SELECT e FROM Encryptionfiles e WHERE e.enFileId = :enFileId"),
    @NamedQuery(name = "Encryptionfiles.findByPath", query = "SELECT e FROM Encryptionfiles e WHERE e.path = :path"),
    @NamedQuery(name = "Encryptionfiles.findByAttributes", query = "SELECT e FROM Encryptionfiles e WHERE e.attributes = :attributes")})
public class Encryptionfiles implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "en_file_id")
    private Long enFileId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "path")
    private String path;
    
    @Size(max = 255)
    @Column(name = "attributes")
    private String attributes;

    public Encryptionfiles() {
    }

    public Encryptionfiles(Long enFileId) {
        this.enFileId = enFileId;
    }

    public Encryptionfiles(Long enFileId, String path) {
        this.enFileId = enFileId;
        this.path = path;
    }

    public Long getEnFileId() {
        return enFileId;
    }

    public void setEnFileId(Long enFileId) {
        this.enFileId = enFileId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (enFileId != null ? enFileId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Encryptionfiles)) {
            return false;
        }
        Encryptionfiles other = (Encryptionfiles) object;
        if ((this.enFileId == null && other.enFileId != null) || (this.enFileId != null && !this.enFileId.equals(other.enFileId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.xfiles.core.entity.Encryptionfiles[ enFileId=" + enFileId + " ]";
    }
    
}
