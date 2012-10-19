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
@Table(name = "user_session")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserSession.findAll", query = "SELECT u FROM UserSession u"),
    @NamedQuery(name = "UserSession.findBySessionId", query = "SELECT u FROM UserSession u WHERE u.sessionId = :sessionId"),
    @NamedQuery(name = "UserSession.findByIpAdress", query = "SELECT u FROM UserSession u WHERE u.ipAdress = :ipAdress"),
    @NamedQuery(name = "UserSession.findByBrouser", query = "SELECT u FROM UserSession u WHERE u.brouser = :brouser")})
public class UserSession implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "session_id")
    private Long sessionId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ip_adress")
    private String ipAdress;
    @Size(max = 255)
    @Column(name = "brouser")
    private String brouser;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private Users userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sessionId")
    private Collection<Logs> logsCollection;

    public UserSession() {
    }

    public UserSession(Long sessionId) {
        this.sessionId = sessionId;
    }

    public UserSession(Long sessionId, String ipAdress) {
        this.sessionId = sessionId;
        this.ipAdress = ipAdress;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    public String getBrouser() {
        return brouser;
    }

    public void setBrouser(String brouser) {
        this.brouser = brouser;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @XmlTransient
    public Collection<Logs> getLogsCollection() {
        return logsCollection;
    }

    public void setLogsCollection(Collection<Logs> logsCollection) {
        this.logsCollection = logsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sessionId != null ? sessionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserSession)) {
            return false;
        }
        UserSession other = (UserSession) object;
        if ((this.sessionId == null && other.sessionId != null) || (this.sessionId != null && !this.sessionId.equals(other.sessionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.xfiles.core.entity.UserSession[ sessionId=" + sessionId + " ]";
    }
    
}
