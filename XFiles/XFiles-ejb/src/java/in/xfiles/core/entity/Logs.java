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
@Table(name = "logs")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Logs.findAll", query = "SELECT l FROM Logs l"),
    @NamedQuery(name = "Logs.findByLogId", query = "SELECT l FROM Logs l WHERE l.logId = :logId"),
    @NamedQuery(name = "Logs.findByIpAdress", query = "SELECT l FROM Logs l WHERE l.ipAdress = :ipAdress"),
    @NamedQuery(name = "Logs.findByOptions", query = "SELECT l FROM Logs l WHERE l.options = :options"),
    @NamedQuery(name = "Logs.findByMessage", query = "SELECT l FROM Logs l WHERE l.message = :message")})
public class Logs implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "log_id")
    private Long logId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ip_adress")
    private String ipAdress;
    @Size(max = 255)
    @Column(name = "options")
    private String options;
    @Size(max = 1000)
    @Column(name = "message")
    private String message;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private Users userId;
    @JoinColumn(name = "session_id", referencedColumnName = "session_id")
    @ManyToOne(optional = false)
    private UserSession sessionId;
    @JoinColumn(name = "type_action_id", referencedColumnName = "action_type_id")
    @ManyToOne(optional = false)
    private ActionTypes typeActionId;

    public Logs() {
    }

    public Logs(Long logId) {
        this.logId = logId;
    }

    public Logs(Long logId, String ipAdress) {
        this.logId = logId;
        this.ipAdress = ipAdress;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public UserSession getSessionId() {
        return sessionId;
    }

    public void setSessionId(UserSession sessionId) {
        this.sessionId = sessionId;
    }

    public ActionTypes getTypeActionId() {
        return typeActionId;
    }

    public void setTypeActionId(ActionTypes typeActionId) {
        this.typeActionId = typeActionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logId != null ? logId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Logs)) {
            return false;
        }
        Logs other = (Logs) object;
        if ((this.logId == null && other.logId != null) || (this.logId != null && !this.logId.equals(other.logId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Logs[ logId=" + logId + " ]";
    }
    
}
