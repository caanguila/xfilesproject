/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigInteger;
/**
 *
 * @author 7
 */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findByUserId", query = "SELECT u FROM Users u WHERE u.userId = :userId"),
    @NamedQuery(name = "Users.findByName", query = "SELECT u FROM Users u WHERE u.name = :name"),
    @NamedQuery(name = "Users.findBySurname", query = "SELECT u FROM Users u WHERE u.surname = :surname"),
    @NamedQuery(name = "Users.findByDateCreation", query = "SELECT u FROM Users u WHERE u.dateCreation = :dateCreation"),
    @NamedQuery(name = "Users.findByDateSuspended", query = "SELECT u FROM Users u WHERE u.dateSuspended = :dateSuspended"),
    @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email"),
    @NamedQuery(name = "Users.findByInformation", query = "SELECT u FROM Users u WHERE u.information = :information")})
public class Users implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "BIGSERIAL")
    private Long userId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "surname")
    private String surname;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_creation")
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    
    @Column(name = "date_suspended")
    @Temporal(TemporalType.DATE)
    private Date dateSuspended;
    
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    
    @Size(max = 2000)
    @Column(name = "information")
    private String information;
    
    @Lob
    @Column(name = "photo")
    private byte[] photo;
    
    @ManyToMany(mappedBy = "usersCollection")
    private Collection<Files> filesCollection;
    
    @ManyToMany(mappedBy = "usersCollection")
    private Collection<Groups> groupsCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "createdBy")
    private Collection<Files> filesCollection1;
    
    @JoinColumn(name = "type_id", referencedColumnName = "type_id")
    @ManyToOne(optional = false)
    private Types typeId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<UserSession> userSessionCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<Logs> logsCollection;
    
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "users")
    private UsersPasswords usersPasswords;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipientId")
    private Collection<Messages> messagesCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "senderId")
    private Collection<Messages> messagesCollection1;

    public Users() {
    }

    public Users(Long userId) {
        this.userId = userId;
    }

    public Users(Long userId, String name, String surname, Date dateCreation, String email) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.dateCreation = dateCreation;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateSuspended() {
        return dateSuspended;
    }

    public void setDateSuspended(Date dateSuspended) {
        this.dateSuspended = dateSuspended;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @XmlTransient
    public Collection<Files> getFilesCollection() {
        return filesCollection;
    }

    public void setFilesCollection(Collection<Files> filesCollection) {
        this.filesCollection = filesCollection;
    }

    @XmlTransient
    public Collection<Groups> getGroupsCollection() {
        return groupsCollection;
    }

    public void setGroupsCollection(Collection<Groups> groupsCollection) {
        this.groupsCollection = groupsCollection;
    }

    @XmlTransient
    public Collection<Files> getFilesCollection1() {
        return filesCollection1;
    }

    public void setFilesCollection1(Collection<Files> filesCollection1) {
        this.filesCollection1 = filesCollection1;
    }

    public Types getTypeId() {
        return typeId;
    }

    public void setTypeId(Types typeId) {
        this.typeId = typeId;
    }

    @XmlTransient
    public Collection<UserSession> getUserSessionCollection() {
        return userSessionCollection;
    }

    public void setUserSessionCollection(Collection<UserSession> userSessionCollection) {
        this.userSessionCollection = userSessionCollection;
    }

    @XmlTransient
    public Collection<Logs> getLogsCollection() {
        return logsCollection;
    }

    public void setLogsCollection(Collection<Logs> logsCollection) {
        this.logsCollection = logsCollection;
    }

    public UsersPasswords getUsersPasswords() {
        return usersPasswords;
    }

    public void setUsersPasswords(UsersPasswords usersPasswords) {
        this.usersPasswords = usersPasswords;
    }

    @XmlTransient
    public Collection<Messages> getMessagesCollection() {
        return messagesCollection;
    }

    public void setMessagesCollection(Collection<Messages> messagesCollection) {
        this.messagesCollection = messagesCollection;
    }

    @XmlTransient
    public Collection<Messages> getMessagesCollection1() {
        return messagesCollection1;
    }

    public void setMessagesCollection1(Collection<Messages> messagesCollection1) {
        this.messagesCollection1 = messagesCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.xfiles.core.entity.Users[ userId=" + userId + " ]";
    }
    
}
