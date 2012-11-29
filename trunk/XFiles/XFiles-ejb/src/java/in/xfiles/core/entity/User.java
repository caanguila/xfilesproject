package in.xfiles.core.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
/**
 *
 * @author 7
 */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByUserId", query = "SELECT u FROM User u WHERE u.userId = :userId"),
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
    @NamedQuery(name = "User.findBySurname", query = "SELECT u FROM User u WHERE u.surname = :surname"),
    @NamedQuery(name = "User.findByDateCreation", query = "SELECT u FROM User u WHERE u.dateCreation = :dateCreation"),
    @NamedQuery(name = "User.findByDateSuspended", query = "SELECT u FROM User u WHERE u.dateSuspended = :dateSuspended"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByInformation", query = "SELECT u FROM User u WHERE u.information = :information")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName="xfiles_seq", name="seq", allocationSize=1, initialValue=1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
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
    private java.sql.Date dateCreation;
    
    @Column(name = "date_suspended")
    private java.sql.Date dateSuspended;
    
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
    
     @JoinTable(name = "users_groups", joinColumns = {
        @JoinColumn(name = "users_user_id", referencedColumnName = "user_id")}, inverseJoinColumns = {
        @JoinColumn(name = "groups_group_id", referencedColumnName = "group_id")})
    @ManyToMany
    private Collection<Groups> groupsCollection;
    
    @JoinColumn(name = "type_id", referencedColumnName = "type_id")
    @ManyToOne
    private Types typeId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Set<UserSession> userSessionCollection;

    public User() {
    }

    public User(Long userId) {
        this.userId = userId;
    }

    public User(Long userId, String name, String surname, java.sql.Date dateCreation, String email) {
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

    public java.sql.Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(java.sql.Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public java.sql.Date getDateSuspended() {
        return dateSuspended;
    }

    public void setDateSuspended(java.sql.Date dateSuspended) {
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

    public Collection<Files> getFilesCollection() {
        return filesCollection;
    }

    public void setFilesCollection(Collection<Files> filesCollection) {
        this.filesCollection = filesCollection;
    }

    public Collection<Groups> getGroupsCollection() {
        return groupsCollection;
    }

    public void setGroupsCollection(Collection<Groups> groupsCollection) {
        this.groupsCollection = groupsCollection;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
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
