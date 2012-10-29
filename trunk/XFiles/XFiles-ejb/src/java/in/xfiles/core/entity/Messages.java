/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Table(name = "messages")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Messages.findAll", query = "SELECT m FROM Messages m"),
    @NamedQuery(name = "Messages.findByMessageId", query = "SELECT m FROM Messages m WHERE m.messageId = :messageId"),
    @NamedQuery(name = "Messages.findByDateSend", query = "SELECT m FROM Messages m WHERE m.dateSend = :dateSend"),
    @NamedQuery(name = "Messages.findByGroupId", query = "SELECT m FROM Messages m WHERE m.groupId = :groupId"),
    @NamedQuery(name = "Messages.findByMessage", query = "SELECT m FROM Messages m WHERE m.message = :message"),
    @NamedQuery(name = "Messages.findByDateRecieved", query = "SELECT m FROM Messages m WHERE m.dateRecieved = :dateRecieved")})
public class Messages implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName="xfiles_seq", name="seq", allocationSize=1, initialValue=1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
    @Column(name = "message_id",columnDefinition = "BIGSERIAL")
    private Long messageId;
    
    @Column(name = "date_send")
    @Temporal(TemporalType.DATE)
    private Date dateSend;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "group_id")
    private long groupId;
    
    @Size(max = 5000)
    @Column(name = "message")
    private String message;
    
    @Column(name = "date_recieved")
    @Temporal(TemporalType.DATE)
    private Date dateRecieved;
    
    @JoinColumn(name = "recipient_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User recipientId;
    
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User senderId;
    
    @JoinColumn(name = "type_id", referencedColumnName = "type_id")
    @ManyToOne(optional = false)
    private Types typeId;

    public Messages() {
    }

    public Messages(Long messageId) {
        this.messageId = messageId;
    }

    public Messages(Long messageId, long groupId) {
        this.messageId = messageId;
        this.groupId = groupId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Date getDateSend() {
        return dateSend;
    }

    public void setDateSend(Date dateSend) {
        this.dateSend = dateSend;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateRecieved() {
        return dateRecieved;
    }

    public void setDateRecieved(Date dateRecieved) {
        this.dateRecieved = dateRecieved;
    }

    public User getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(User recipientId) {
        this.recipientId = recipientId;
    }

    public User getSenderId() {
        return senderId;
    }

    public void setSenderId(User senderId) {
        this.senderId = senderId;
    }

    public Types getTypeId() {
        return typeId;
    }

    public void setTypeId(Types typeId) {
        this.typeId = typeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (messageId != null ? messageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Messages)) {
            return false;
        }
        Messages other = (Messages) object;
        if ((this.messageId == null && other.messageId != null) || (this.messageId != null && !this.messageId.equals(other.messageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.xfiles.core.entity.Messages[ messageId=" + messageId + " ]";
    }
    
}
