package in.xfiles.core.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
/**
 *
 * @author 7
 */
@Entity
@Table(name = "action_types")
@NamedQueries({
    @NamedQuery(name = "ActionTypes.findAll", query = "SELECT a FROM ActionTypes a"),
    @NamedQuery(name = "ActionTypes.findByActionTypeId", query = "SELECT a FROM ActionTypes a WHERE a.actionTypeId = :actionTypeId"),
    @NamedQuery(name = "ActionTypes.findByName", query = "SELECT a FROM ActionTypes a WHERE a.name = :name"),
    @NamedQuery(name = "ActionTypes.findByDescription", query = "SELECT a FROM ActionTypes a WHERE a.description = :description")})
public class ActionTypes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName="xfiles_seq", name="seq", allocationSize=1, initialValue=1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
    @Column(name = "action_type_id", columnDefinition="BIGSERIAL")
    private Long actionTypeId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    
    @Size(max = 2000)
    @Column(name = "description")
    private String description;

    public ActionTypes() {
    }

    public ActionTypes(Long actionTypeId) {
        this.actionTypeId = actionTypeId;
    }

    public ActionTypes(Long actionTypeId, String name) {
        this.actionTypeId = actionTypeId;
        this.name = name;
    }

    public Long getActionTypeId() {
        return actionTypeId;
    }

    public void setActionTypeId(Long actionTypeId) {
        this.actionTypeId = actionTypeId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actionTypeId != null ? actionTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActionTypes)) {
            return false;
        }
        ActionTypes other = (ActionTypes) object;
        if ((this.actionTypeId == null && other.actionTypeId != null) || (this.actionTypeId != null && !this.actionTypeId.equals(other.actionTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.xfiles.core.entity.ActionTypes[ actionTypeId=" + actionTypeId + " ]";
    }
    
}
