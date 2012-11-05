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
            FileUtils.moveFile(tmpFile, targetFile);
        } catch (Exception ex) {
            log.error("encryptFile(): Failed to encrypt file: source = "
                            + sourceFile.getAbsolutePath()
                            + ", target = " + sourceFile.getAbsolutePath());
            throw new RuntimeException("Failed to encrypt file");
        }
    }

    @Override
    public void decryptFile(File sourceFile, File targetFile) {
        try {
            File tmpFile = CryptoHelper.deCryptFile(targetFile, "AES", getKey());
            FileUtils.moveFile(tmpFile, targetFile);
        } catch (Exception ex) {
            log.error("decryptFile(): Failed to decrypt file: source = "
                            + sourceFile.getAbsolutePath()
                            + ", target = " + sourceFile.getAbsolutePath());
            throw new RuntimeException("Failed to decrypt file");
        }
    }
    
}
