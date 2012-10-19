/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author 7
 */
@Embeddable
public class PasswordStoragePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private long userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "enc_type_def_id")
    private long encTypeDefId;

    public PasswordStoragePK() {
    }

    public PasswordStoragePK(long userId, long encTypeDefId) {
        this.userId = userId;
        this.encTypeDefId = encTypeDefId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getEncTypeDefId() {
        return encTypeDefId;
    }

    public void setEncTypeDefId(long encTypeDefId) {
        this.encTypeDefId = encTypeDefId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) encTypeDefId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PasswordStoragePK)) {
            return false;
        }
        PasswordStoragePK other = (PasswordStoragePK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.encTypeDefId != other.encTypeDefId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.xfiles.core.entity.PasswordStoragePK[ userId=" + userId + ", encTypeDefId=" + encTypeDefId + " ]";
    }
    
}
