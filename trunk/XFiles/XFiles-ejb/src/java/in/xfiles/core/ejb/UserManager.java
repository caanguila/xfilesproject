package in.xfiles.core.ejb;

import in.xfiles.core.entity.Types;
import in.xfiles.core.entity.User;
import in.xfiles.core.entity.UsersPasswords;
import in.xfiles.core.helpers.CommonConstants;
import in.xfiles.core.helpers.CommonTools;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author 7
 */
@Stateless
@LocalBean
public class UserManager implements UserManagerLocal {

    private final Logger log = Logger.getLogger(UserManager.class);
    @PersistenceContext
    private EntityManager em;
    @EJB
    private LogManagerLocal lm;
    @EJB
    private SessionManagerLocal sm;

    @Override
    public User createUser(Map<String, Object> params) {
        User user = null;
        //TO-DO Implementation
        return user;
    }

    @Override
    public User createUser(Long typeID, String email, String name, String surname, String password, String information) {
        Types otype = em.find(Types.class, typeID);

        if (otype != null) {

            User user = new User();
            user.setName(name);
            user.setSurname(surname);
            user.setDateCreation(new Date());
            user.setEmail(email);
            user.setInformation(information);
            user.setTypeId(otype);

            em.persist(user);

            UsersPasswords up = new UsersPasswords(user.getUserId());
            up.setLogin(email);
            up.setPassword(password);
            em.persist(up);

            log.info("createUser: User created: " + user);

            return user;
        } else {
            log.warn("createUser(): TypeID not found: " + typeID);
            return null;
        }
    }

    @Override
    public boolean removeUserById(Long userId) {
        User u = getUserById(userId);
        if (u == null) {
            return false;
        }
        em.remove(u);
        return false;
    }

    @Override
    public User getUserById(Long userId) {
        Query q = em.createNamedQuery("User.findByUserId", User.class);
        q.setParameter("userId", userId);
        return (User) q.getSingleResult();
    }

    @Override
    public Long tryLogin(String login, String pwd) {
        Query q = em.createNamedQuery("UsersPasswords.findByLoginAndPassword", UsersPasswords.class);
        q.setMaxResults(1);
        List<UsersPasswords> l = q.getResultList();
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0).getUserId();
    }

    @Override
    public User getUserByLogin(String login) {
        UsersPasswords up = CommonTools.getFirstElement(
                em.createNamedQuery("UsersPasswords.findByLogin", UsersPasswords.class).setParameter("login", login).setMaxResults(1).getResultList());
        if (up == null) {
            return null;
        }
        return getUserById(up.getUserId());
    }

    @Override
    public User modifyUserInfo(Long userId, String name, String surname, String information) {
        if (userId == null) {
            return null;
        } else {
            User user = em.find(User.class, userId);
            if (user != null) {
                user.setName(name);
                user.setSurname(surname);
                user.setInformation(information);
                em.merge(user);
                log.info("User information modyfyed: " + user);
                String session = sm.getUserSession(userId).getSession();
                lm.addRecord(userId, CommonConstants.CHANGE_PROFILE, "", "" + new java.util.Date(), session);//TO-DO
            }
            return user;
        }
    }
}
