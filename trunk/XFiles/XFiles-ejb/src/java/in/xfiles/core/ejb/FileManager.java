package in.xfiles.core.ejb;

import in.xfiles.core.crypto.FileEncryptor;
import in.xfiles.core.entity.Files;
import in.xfiles.core.helpers.CommonConstants;
import in.xfiles.core.helpers.CryptoHelper;
import in.xfiles.core.wrappers.UploadedFileWrapper;
import java.io.File;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 *
 * @author 7
 */
@Stateless
public class FileManager implements FileManagerLocal{

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
//            FileEncryptor engine = FileEncryptor.getEncryptor(ufw.getEncryptionType());
//            engine.setKey(ufw.getKey());
//            File targetFile = File.createTempFile("xfiles_", "."+ufw.getEncryptionType());
//            engine.encryptFile(ufw.getFile(), targetFile, true);
//            Files f = new Files();
//            f.setCrc(ufw.getChecksum());
//            f.setContentType(ufw.getContentType());
//            f.setName(ufw.getName());
//            f.setSize(ufw.getSize());
//            f.setIsfolder(false);
//            //f.setTypeId();
//            //f.setParentId();
//            f.setCreatedBy(ufw.getUploadedBy());
//            // owners!!!
//            em.persist(f);
        } catch (Exception ex) {
            log.error("processFile(): operation failed due to Exception", ex);
        }
    }
    
}
