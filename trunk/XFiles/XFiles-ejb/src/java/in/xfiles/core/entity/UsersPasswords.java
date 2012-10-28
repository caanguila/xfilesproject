/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Table(name = "users_passwords")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsersPasswords.findAll", query = "SELECT u FROM UsersPasswords u"),
    @NamedQuery(name = "UsersPasswords.findByUserId", query = "SELECT u FROM UsersPasswords u WHERE u.userId = :userId"),
    @NamedQuery(name = "UsersPasswords.findByPassword", query = "SELECT u FROM UsersPasswords u WHERE u.password = :password"),
    @NamedQuery(name = "UsersPasswords.findByLogin", query = "SELECT u FROM UsersPasswords u WHERE u.login = :login"),
    @NamedQuery(name = "UsersPasswords.findByLoginAndPassword", query="SELECT u FROM UsersPasswords u WHERE u.login = :login AND u.password = :password")
})
public class UsersPasswords implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName="xfiles_seq", name="seq", allocationSize=1, initialValue=1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
    @Column(name = "user_id",columnDefinition = "BIGSERIAL")
    private Long userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "login")
    private String login;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private User users;

    public UsersPasswords() {
    }

    public UsersPasswords(Long userId) {
        this.userId = userId;
    }

    public UsersPasswords(Long userId, String password, String login) {
        this.userId = userId;
        this.password = password;
        this.login = login;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
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
        if (!(object instanceof UsersPasswords)) {
            return false;
        }
        UsersPasswords other = (UsersPasswords) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "in.xfiles.core.entity.UsersPasswords[ userId=" + userId + " ]";
    }
    
}
