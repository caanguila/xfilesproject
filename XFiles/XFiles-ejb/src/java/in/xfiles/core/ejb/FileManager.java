package in.xfiles.core.ejb;

import in.xfiles.core.crypto.FileEncryptor;
import in.xfiles.core.entity.*;
import in.xfiles.core.helpers.CommonConstants;
import in.xfiles.core.helpers.CommonTools;
import in.xfiles.core.helpers.CryptoHelper;
import in.xfiles.core.helpers.ShamirSchema;
import in.xfiles.core.wrappers.UploadedFileWrapper;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import java.util.*;
import java.util.logging.Level;
import javax.persistence.Query;

/**
 *
 * @author 7
 */
@Stateless
public class FileManager implements FileManagerLocal, CommonConstants {

    public static final String FILE_UPLOAD_DIRECTORY = "uploads/";
    public static final String REQUESTED_DOWNLOADS_DIRECTORY = "uploads/downloads/";
    private Logger log = Logger.getLogger(FileManager.class);
    @PersistenceContext
    private EntityManager em;
    @EJB
    private LogManagerLocal lm;
    @EJB
    private UserManagerLocal um;
    @EJB
    private GroupManagerLocal gml;
    @EJB
    private MessageManagerLocal mml;

    @Override
    @Asynchronous
    public void processFile(UploadedFileWrapper ufw) {
        log.debug(">> processFile(): Started asynchronous processing of uploaded file: " + ufw);
        if (!FileEncryptor.getSupportedEngines().contains(ufw.getEncryptionType())) {
            log.debug("processFile(): Usupported encryption engine: " + ufw.getEncryptionType());
            return;
        }
        try {
            FileEncryptor engine = FileEncryptor.getEncryptor(ufw.getEncryptionType());
            engine.setKey(ufw.getKey());

            File targetTmpFile = File.createTempFile("xfiles_", "." + ufw.getEncryptionType());
            engine.encryptFile(ufw.getFile(), targetTmpFile, true);
            FileUtils.moveFileToDirectory(targetTmpFile, new File(FILE_UPLOAD_DIRECTORY), true);
            File targetFile = new File(FILE_UPLOAD_DIRECTORY, targetTmpFile.getName());

            Files f = new Files();
            f.setCrc(ufw.getChecksum());
            f.setContentType(ufw.getContentType());
            f.setName(ufw.getName());
            f.setSize(ufw.getSize());
            f.setIsfolder(false);

            if ("private".equalsIgnoreCase(ufw.getAccessType())) {
                f.setTypeId(em.find(Types.class, PRIVATE_FILE_TYPE));
            } else if ("public".equalsIgnoreCase(ufw.getAccessType())) {
                f.setTypeId(em.find(Types.class, PUBLIC_FILE_TYPE));
            } else if ("group".equalsIgnoreCase(ufw.getAccessType())) {
                f.setTypeId(em.find(Types.class, GROUP_FILE_TYPE));
            } else {
                throw new IllegalArgumentException("Incorrect access type provided: " + ufw.getAccessType());
            }

            f.setParentId(null); // TODO: parent file
            f.setCreatedBy(ufw.getUploadedBy());

            if (!"group".equalsIgnoreCase(ufw.getAccessType())) {
                Collection<User> owners = new HashSet<User>();
                owners.add(ufw.getUploadedBy());
                f.setUsersCollection(owners);

                Encryptionfiles ef = new Encryptionfiles();
                ef.setPath(targetFile.getAbsolutePath());
                f.setEnFileId(ef);

                PasswordStorage ps = new PasswordStorage();
                ps.setUserId(ufw.getUploadedBy().getUserId());
                ps.setPassword(ufw.getKey());
                log.debug("File upload key: " + ufw.getKey());
                Collection psCollection = new HashSet<PasswordStorage>();
                psCollection.add(ps);
                if ("AES".equals(ufw.getEncryptionType())) {
                    f.setEncTypeId(em.find(Types.class, AES_ENCRYPTION_TYPE));
                } else if ("plain".equals(ufw.getEncryptionType())) {
                    f.setEncTypeId(em.find(Types.class, PLAIN_ENCRYPTION_TYPE));
                } else {
                    throw new IllegalArgumentException("Unsupported encryption type: " + ufw.getEncryptionType());
                }
                // owners!!!
                em.persist(ef);
                em.persist(ps);
                em.persist(f);
                em.flush();

            } else {
                int k = 3;
                // minimum parts
                User u1 = em.find(User.class, 2640L);
                User u2 = em.find(User.class, 2649L);
                User u3 = em.find(User.class, 2675L);
                User u4 = em.find(User.class, 2685L);
                User u5 = em.find(User.class, 2691L);
                Collection<User> owners = new HashSet<User>();
                owners.add(u1);
                owners.add(u2);
                owners.add(u3);
                owners.add(u4);
                owners.add(u5);
                persistGroupFile(f, ufw, targetFile, owners, k);
            }
            String session = getCurrentUserSession(ufw.getUploadedBy());
            lm.addRecord(ufw.getUploadedBy().getUserId(), CommonConstants.UPLOAD_COMPLETE, "Upload Complete", "", session);

        } catch (Exception ex) {
            log.error("processFile(): operation failed due to Exception", ex);
        }
    }

    // parametr k stores in table encryption files
    private void persistGroupFile(Files f, UploadedFileWrapper ufw, File targetFile, Collection<User> owners, int k) {
        log.info("Persisting of Group file!");

        if ("AES".equals(ufw.getEncryptionType())) {
            f.setEncTypeId(em.find(Types.class, AES_ENCRYPTION_TYPE));
        } else {
            throw new IllegalArgumentException("Unsupported encryption type: " + ufw.getEncryptionType());
        }

        if (owners.isEmpty()) {
            throw new IllegalArgumentException("Number of Users should be > 0 ");
        }
        if (k > owners.size()) {
            throw new IllegalArgumentException("Number of Users should be more then k: " + k + "  actual: " + owners.size());
        }
        //Group creation
        Groups gr = gml.createGroup(owners, "Group for file: " + f.getName(), "", CommonConstants.PRIVATE_GROUP);
        log.info("group created: " + gr.getGroupId());
        //work with file
        f.setUsersCollection(owners); //who can see this file

        Encryptionfiles ef = new Encryptionfiles();
        ef.setPath(targetFile.getAbsolutePath());
        ef.setAttributes("" + k);
        log.info("Persisting of encrFiles");
        em.persist(ef);
        log.info("encFiles persisted: " + ef.getEnFileId());
        f.setEnFileId(ef);  // from where we can download this file

        log.info("Users share secret: " + f.getUsersCollection().size() + " with minimum parts: " + ef.getAttributes());


        String secret = ufw.getKey();
        HashMap<Integer, String> parts = ShamirSchema.splitShare(secret, owners.size(), k); //Share secret
        Collection<PasswordStorage> pswds = new ArrayList<PasswordStorage>(); // Passwords for one file
        int i = 1;
        for (User u : f.getUsersCollection()) {
            PasswordStorage ps = new PasswordStorage();
            ps.setUserId(u.getUserId());
            ps.setPassword(parts.get(i));
            ps.setOptions("" + i);
            log.info("Persisting of passwordStorage");
            em.persist(ps);
            log.info("Password storage persisted: " + ps.getPasswordStorageId());
            pswds.add(ps);
            log.info("u: " + u.getUserId() + "  ps: " + ps.getPassword());
            i++;
        }
        f.setPasswordStorageCollection(pswds);
        log.info("Password Storage Info " + f.getPasswordStorageCollection().size());
        for (PasswordStorage ps : f.getPasswordStorageCollection()) {
            log.info("user: " + ps.getUserId() + "  password: " + ps.getPassword() + "  Options: " + ps.getOptions());
        }
        log.info("Persisting of file");
        em.persist(f);
        log.info("file Persisted: " + f.getFileId());

        Collection col = gr.getFilesCollection();
        col.add(f);
        gr.setFilesCollection(col);
        log.info("Merging of Group: " + gr.getFilesCollection().size());
        em.persist(gr);

        em.getEntityManagerFactory().getCache().evictAll();


    }

    private String getCurrentUserSession(User u) {
        String session = "";

        Query q = em.createQuery("select u from UserSession u where u.userId =:userID");
        q.setParameter("userID", u);
        List result = q.getResultList();
        Iterator iter = result.iterator();
        UserSession ses = null;
        while (iter.hasNext()) {

            UserSession one = (UserSession) iter.next();
            log.info("One: " + one.getSessionId() + " session: " + one.getSession());
            if (ses == null) {
                ses = one;
            } else if (one.getSessionId().compareTo(ses.getSessionId()) > 0) {
                ses = one;
            }
        }
        if (ses != null) {
            session = ses.getSession();
        }
        log.info("Session: " + ses.getSessionId() + "   user: " + ses.getUserId() + " session: " + ses.getSession());
        return session;
    }

    @Override
    @Asynchronous
    public void requestDownload(long userId, long fileId, String key) {
        User u = um.getUserById(userId);
        if (u == null) {
            log.debug("requestDownload(): there is no user with ID = " + userId);
            return;
        }
        Files file = em.find(Files.class, fileId);
        if (file == null) {
            log.debug("requestDownload(): there is no file with ID = " + fileId);
            return;
        }
        Collection<User> owners = file.getUsersCollection();
        if (owners == null || owners.isEmpty()) {
            log.error("The list of file owners is empty or null! fileId=" + fileId);
            return;
        }

        if (!PUBLIC_FILE_TYPE.equals(file.getTypeId().getTypeId()) && !owners.contains(u)) {
            log.debug("requestDownload(): permission denied. fileId = " + fileId);
            return;
        }

        try {
          if(!file.getTypeId().getTypeId().equals(CommonConstants.GROUP_FILE_TYPE)){
            DownloadRequest request = new DownloadRequest();
            request.setUser(u);
            request.setFile(file);
            request.setDateRequested(new Date());
            request.setProperties(u.getUserId() + ":" + key);
            em.persist(request);

            // TODO: validate user key
            request.setStatus(DownloadRequest.APPROVED_STATUS);
            request = em.merge(request);

            FileEncryptor engine = FileEncryptor.getEncryptor(file.getEncTypeId().getName());
            engine.setKey(key);
            log.debug("FIle download key: " + key + " encrypt path: " + file.getEnFileId().getPath());
            File downloadDirectory = new File(REQUESTED_DOWNLOADS_DIRECTORY);
            downloadDirectory.mkdirs();
            File targetFile = File.createTempFile("download_", ".tmp", downloadDirectory);
            engine.decryptFile(new File(file.getEnFileId().getPath()), targetFile);

            // TODO: create zip archive and send the link and password via email

            request.setDateProvided(new Date());
            request.setOutputFile(targetFile.getAbsolutePath());
            request.setStatus(DownloadRequest.READY_STATUS);
            em.persist(request);
            em.flush();

            log.trace("requestDownload(): file has been decrypted: output = " + targetFile.getAbsolutePath());
            lm.addRecord(userId, DOWNLOAD_COMPLETE, "Decryption completed", targetFile.getAbsolutePath(), null);
        }else{
            
            groupDownloadRequest(file, u);
        }
          
        } catch (Exception ex) {
        }
    }

    private void groupDownloadRequest(Files file, User user){
        log.debug("Request for group file!!!");
        DownloadRequest request = new DownloadRequest();
            request.setUser(user);
            request.setFile(file);
            request.setDateRequested(new Date());
            request.setStatus(DownloadRequest.APPROVED_STATUS);
            request.setProperties("");
            em.persist(request);
            Groups group = null;
            for(Groups gr: file.getGroupsCollection()){
             //   log.debug(gr.getName()+"  "+gr.getUsersCollection().contains(user));
                group = gr;
            }
            if(group == null) {
                log.debug("Bad, very bad");
                return;
            }
            //proff
            //Send messages to Users of this file
            String message = "user with Id: "+user.getUserId()+" want to download fileID: "+file.getFileId()+" name: "+file.getName();
            mml.sendGruopMessage(group.getGroupId(),message , CommonConstants.GROUP_ACCESS_MESSAGE, user.getUserId());
            for(User u:group.getUsersCollection()){
                log.debug("Should send mess to "+u);
                
            }
    }
    
    @Override
    @Asynchronous
    public void completeGroupFile(DownloadRequest request) {
        try {
            String prop = request.getProperties();
            ArrayList result = CommonTools.parceElements(prop);
            HashMap secretParts = new HashMap();
            Collection<PasswordStorage> col = request.getFile().getPasswordStorageCollection();
            log.debug("col: "+col.size());
            for(int i=0; i<result.size(); i+=2){
                String val = (String) result.get(i+1);
                log.debug(result.get(i)+" value: "+val);
                
                    for(PasswordStorage ps : col){
                        if(val.equals(ps.getUserId()+"")){
                            Integer x = new Integer(ps.getOptions());
                            secretParts.put(x, ps.getPassword());
                        }
                    }
                
            }
            log.debug("parts size: "+secretParts.keySet().size());
            String secret = ShamirSchema.combineSecret(secretParts); //combine Secret
            log.debug("Secret: "+secret);
            Files file = request.getFile();
            
                FileEncryptor engine = FileEncryptor.getEncryptor(file.getEncTypeId().getName());
                engine.setKey(secret);
                log.debug("FIle download key: " + secret + " encrypt path: " + file.getEnFileId().getPath());
                File downloadDirectory = new File(FileManager.REQUESTED_DOWNLOADS_DIRECTORY);
                downloadDirectory.mkdirs();
                File targetFile = File.createTempFile("download_", ".tmp", downloadDirectory);
                engine.decryptFile(new File(file.getEnFileId().getPath()), targetFile);

                // TODO: create zip archive and send the link and password via email

                request.setDateProvided(new Date());
                request.setOutputFile(targetFile.getAbsolutePath());
                request.setStatus(DownloadRequest.READY_STATUS);
                em.merge(request);
                em.flush();

                log.trace("requestDownload(): file has been decrypted: output = " + targetFile.getAbsolutePath());
                lm.addRecord(request.getUser().getUserId(), FileManager.DOWNLOAD_COMPLETE, "Decryption completed", targetFile.getAbsolutePath(), null);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SequreManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SequreManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void testDatabase(Long userId) {
        log.debug(em);

        log.debug("Creation of password storage");
        Types t = em.find(Types.class, 9L);
        log.debug("PSW type: " + t.getName());

        PasswordStorage ps = new PasswordStorage();
        ps.setPassword("qwerty");

        ps.setUserId(1036);

        log.debug("Creation of enc files");

        Encryptionfiles ef = new Encryptionfiles();
        ef.setPath("SomePath");

        log.debug("Persisting of passwords and encfyle");
        em.persist(ef);
        em.persist(ps);
        log.debug("ID of encfiles: " + ef.getEnFileId());
        log.debug("ID of pasw_Stor " + ps.getPasswordStorageId());
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
        log.debug("Persisting of file for user " + userId);
        em.persist(file);
        em.getEntityManagerFactory().getCache().evictAll();


    }

    @Override
    public Collection<Files> getFilesByUser(Long userId) {
        if (userId == null) {
            return Collections.EMPTY_LIST;
        }
        User user = em.find(User.class, userId);

        if (user != null) {
            em.getEntityManagerFactory().getCache().evictAll();
            return user.getFilesCollection();
        } else {
            return null;
        }
    }

    @Override
    public boolean fileExists(Long userId, long fileId) {
        if (userId == null) {
            return false;
        }
        Files f = em.find(Files.class, fileId);
        if (f == null) {
            return false;
        }
        if (PUBLIC_FILE_TYPE.equals(f.getTypeId().getTypeId())) {
            return true;
        }
        if (f.getUsersCollection().contains(em.find(User.class, userId))) {
            return true;
        }
        return false;
    }

    @Override
    public Files getFileById(Long userId, long fileId) {
        if (fileExists(userId, fileId)) {
            return em.find(Files.class, fileId);
        } else {
            return null;
        }
    }

    @Override
    public List<DownloadRequest> getRequestsByUserId(Long userId) {
        if (userId == null) {
            return Collections.EMPTY_LIST;
        }
        return em.createQuery("SELECT r FROM DownloadRequest r WHERE r.user.userId = :userId order by r.dateRequested", DownloadRequest.class).setParameter("userId", userId).getResultList();
    }

    @Override
    public DownloadRequest getRequestsById(Long requestId) {
        if (requestId == null) {
            return null;
        }
        return em.find(DownloadRequest.class, requestId);
    }
}
