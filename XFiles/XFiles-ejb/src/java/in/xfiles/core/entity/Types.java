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
@Table(name = "types")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Types.findAll", query = "SELECT t FROM Types t"),
    @NamedQuery(name = "Types.findByTypeId", query = "SELECT t FROM Types t WHERE t.typeId = :typeId"),
    @NamedQuery(name = "Types.findByCategory", query = "SELECT t FROM Types t WHERE t.category = :category"),
    @NamedQuery(name = "Types.findByName", query = "SELECT t FROM Types t WHERE t.name = :name"),
    @NamedQuery(name = "Types.findByDescription", query = "SELECT t FROM Types t WHERE t.description = :description"),
    @NamedQuery(name = "Types.findByOptions", query = "SELECT t FROM Types t WHERE t.options = :options")})
public class Types implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "type_id")
    private Long typeId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "category")
    private String category;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    
    @Size(max = 1000)
    @Column(name = "description")
    private String description;
    
    @Size(max = 2000)
    @Column(name = "options")
    private String options;

    public Types() {
    }

    public Types(Long typeId) {
        this.typeId = typeId;
    }

    public Types(Long typeId, String category, String name) {
        this.typeId = typeId;
        this.category = category;
        this.name = name;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (typeId != null ? typeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Types)) {
            return false;
        }
        Types other = (Types) object;
        if ((this.typeId == null && other.typeId != null) || (this.typeId != null && !this.typeId.equals(other.typeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.xfiles.core.entity.Types[ typeId=" + typeId + " ]";
    }
    
}
