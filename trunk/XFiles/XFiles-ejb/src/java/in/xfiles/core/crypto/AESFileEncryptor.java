package in.xfiles.core.crypto;

import in.xfiles.core.helpers.CryptoHelper;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * A simple implementation of AES file encryptor.
 * 
 * @author danon
 */
public class AESFileEncryptor extends FileEncryptor {
    
    private final Logger log = Logger.getLogger(AESFileEncryptor.class);

    @Override
    public void encryptFile(File sourceFile, File targetFile, boolean delete) {
        try {
            File tmpFile = CryptoHelper.enCryptFile(targetFile, "AES", getKey());
            if(delete)
                FileUtils.forceDelete(sourceFile);
            FileUtils.forceDelete(targetFile);
            FileUtils.moveFile(tmpFile, targetFile);
        } catch (Exception ex) {
            log.error("encryptFile(): Failed to encrypt file: source = "
                            + sourceFile.getAbsolutePath()
                            + ", target = " + sourceFile.getAbsolutePath(), ex);
            throw new RuntimeException("Failed to encrypt file", ex);
        }
    }

    @Override
    public void decryptFile(File sourceFile, File targetFile) {
        try {
            File tmpFile = CryptoHelper.deCryptFile(targetFile, "AES", getKey());
            FileUtils.forceDelete(targetFile);
            FileUtils.moveFile(tmpFile, targetFile);
        } catch (Exception ex) {
            log.error("decryptFile(): Failed to decrypt file: source = "
                            + sourceFile.getAbsolutePath()
                            + ", target = " + sourceFile.getAbsolutePath(), ex);
            throw new RuntimeException("Failed to decrypt file", ex);
        }
    }
    
}
