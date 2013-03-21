package in.xfiles.core.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

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
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "session_id")
    private Long sessionId;
    
   
    @Size(min = 1, max = 255)
    @Column(name = "ip_adress")
    private String ipAdress;
    
    @Size(max = 255)
    @Column(name = "brouser")
    private String brouser;
    
    @Size(max = 255)
    @Column(name = "session_name")
    private String session;
    
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable=true)
    @ManyToOne
    private User userId;
    
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

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
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

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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
