package in.xfiles.core.entity;

import java.io.Serializable;
import java.util.Date;
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
    @NamedQuery(name = "Log.findAll", query = "SELECT l FROM Log l"),
    @NamedQuery(name = "Log.findByLogId", query = "SELECT l FROM Log l WHERE l.logId = :logId"),
    @NamedQuery(name = "Log.findByIpAdress", query = "SELECT l FROM Log l WHERE l.ipAdress = :ipAdress"),
    @NamedQuery(name = "Log.findByOptions", query = "SELECT l FROM Log l WHERE l.options = :options"),
    @NamedQuery(name = "Log.findByMessage", query = "SELECT l FROM Log l WHERE l.message = :message")})
public class Log implements Serializable, Comparable<Log> {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName="xfiles_seq", name="seq", allocationSize=1, initialValue=1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
    @Column(name = "log_id",columnDefinition = "BIGSERIAL")
    private Long logId;
    
    
    
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
    @ManyToOne
    private User userId;
    
    @Column(name="session_name")
    private String session;
    
    @JoinColumn(name = "type_action_id", referencedColumnName = "action_type_id")
    @ManyToOne
    private ActionTypes typeActionId;
   
    @Column(name="date_creation")
    @Temporal(TemporalType.TIMESTAMP)
    Date dateCreation;

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date date_creation) {
        this.dateCreation = date_creation;
    }
    
    
    public Log() {
    }

    public Log(Long logId) {
        this.logId = logId;
    }

    public Log(Long logId, String ipAdress) {
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

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getSessionName() {
        return session;
    }

    public void setSessionName(String session) {
        this.session = session;
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
        if (!(object instanceof Log)) {
            return false;
        }
        Log other = (Log) object;
        if ((this.logId == null && other.logId != null) || (this.logId != null && !this.logId.equals(other.logId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.xfiles.core.entity.Logs[ logId=" + logId + " ]";
    }

    
    @Override
    public int compareTo(Log o) {
      
        if(this.logId > o.getLogId()) return -1;
        else if(this.logId < o.getLogId()) return 1;
        else return 0;
    }
    
}
