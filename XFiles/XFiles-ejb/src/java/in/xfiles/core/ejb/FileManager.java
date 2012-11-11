package in.xfiles.core.ejb;

import in.xfiles.core.crypto.FileEncryptor;
import in.xfiles.core.entity.*;
import in.xfiles.core.helpers.CommonConstants;
import in.xfiles.core.helpers.CryptoHelper;
import in.xfiles.core.wrappers.UploadedFileWrapper;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author 7
 */
@Stateless
public class FileManager implements FileManagerLocal, CommonConstants {
    
    public static final String FILE_UPLOAD_DIRECTORY = "./uploads/";

    private Logger log = Logger.getLogger(FileManager.class);
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private LogManagerLocal lm;
    
    
    
    @Override @Asynchronous
    public void processFile(UploadedFileWrapper ufw) {
        log.debug("Started asynchronous processing of uploaded file: "+ufw);
        if(!FileEncryptor.getSupportedEngines().contains(ufw.getEncryptionType())) {
            log.debug("processFile(): Usupported encryption engine: "+ufw.getEncryptionType());
            return;
        }
        // TODO: implementation
        try {
            FileEncryptor engine = FileEncryptor.getEncryptor(ufw.getEncryptionType());
            engine.setKey(ufw.getKey());
            File targetTmpFile = File.createTempFile("xfiles_", "."+ufw.getEncryptionType());
            engine.encryptFile(ufw.getFile(), targetTmpFile, true);
            FileUtils.moveFileToDirectory(targetTmpFile, new File(FILE_UPLOAD_DIRECTORY), true);
            File targetFile = new File(FILE_UPLOAD_DIRECTORY, targetTmpFile.getName());
            Files f = new Files();
            f.setCrc(ufw.getChecksum());
            f.setContentType(ufw.getContentType());
            f.setName(ufw.getName());
            f.setSize(ufw.getSize());
            f.setIsfolder(false);
            if("private".equalsIgnoreCase(ufw.getAccessType())) {
                f.setTypeId(em.find(Types.class, PRIVATE_FILE_TYPE));
            } else if("public".equalsIgnoreCase(ufw.getAccessType())) {
                f.setTypeId(em.find(Types.class, PUBLIC_FILE_TYPE));
            } else if ("group".equalsIgnoreCase(ufw.getAccessType())) {
                f.setTypeId(em.find(Types.class, GROUP_FILE_TYPE));
            } else throw new IllegalArgumentException("Incorrect access type provided: "+ufw.getAccessType());
            //f.setParentId();
            f.setCreatedBy(ufw.getUploadedBy());
            Collection<User> owners = new HashSet<User>();
            owners.add(ufw.getUploadedBy());
            f.setUsersCollection(owners);
            Encryptionfiles ef = new Encryptionfiles();
            ef.setPath(targetFile.getAbsolutePath());
            f.setEnFileId(ef);
            PasswordStorage ps = new PasswordStorage();
            ps.setUserId(ufw.getUploadedBy().getUserId());
            ps.setPassword(ufw.getKey());
            Collection psCollection = new HashSet<PasswordStorage>();
            psCollection.add(ps);
            if("AES".equals(ufw.getEncryptionType()))
                f.setEncTypeId(em.find(Types.class, AES_ENCRYPTION_TYPE));
            else if("plain".equals(ufw.getEncryptionType()))
                f.setEncTypeId(em.find(Types.class, PLAIN_ENCRYPTION_TYPE));
            else throw new IllegalArgumentException("Unsupported encryption type: "+ufw.getEncryptionType());
            // owners!!!
            em.persist(ef);
            em.persist(ps);
            em.persist(f);
            em.getEntityManagerFactory().getCache().evictAll();
        } catch (Exception ex) {
            log.error("processFile(): operation failed due to Exception", ex);
        }
    }

    @Override
    public void testDatabase(Long userId) {
        log.debug(em);
        
        log.debug("Creation of password storage");
        Types t = em.find(Types.class, 9L);
        log.debug("PSW type: "+t.getName());
        
        PasswordStorage ps = new PasswordStorage();
        ps.setPassword("qwerty");
       
        ps.setUserId(1036);
        
        log.debug("Creation of enc files");
        
        Encryptionfiles ef = new Encryptionfiles();
        ef.setPath("SomePath");
        
        log.debug("Persisting of passwords and encfyle");
        em.persist(ef);
        em.persist(ps);
        log.debug("ID of encfiles: "+ef.getEnFileId());
        log.debug("ID of pasw_Stor "+ps.getPasswordStorageId());
        log.debug("Creation of file");
        
        Files file = new Files();
        file.setContentType("Contenttype");
        file.setName("nyfile");
        file.setDescription("SomeDesct");
        file.setSize(1258964);
        file.setCrc(46845684);
        file.setEnFileId(ef);
        HashSet h = new HashSet();
        h.add(ps);
        file.setPasswordStorageCollection(h);
        file.setIsfolder(false);
        file.setParentId(null);
        file.setEncTypeId(t);
        file.setTypeId(em.find(Types.class, 3L));
        HashSet h1 = new HashSet();
        h1.add(em.find(User.class, userId));
        file.setUsersCollection(h1);
        file.setCreatedBy(em.find(User.class, userId));
        log.debug("Persisting of file for user "+userId);
        em.persist(file);
        em.getEntityManagerFactory().getCache().evictAll();
        
        
    }

    @Override
    public Collection<Files> getFilesByUser(Long usertId) {
        if(usertId == null)
            return Collections.EMPTY_LIST;
        User user = em.find(User.class, usertId);
      
        if(user != null) 
            return user.getFilesCollection();
        else return null;
    }
    
}
