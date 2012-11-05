package in.xfiles.core.crypto;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * A plain text file encryptor.
 * Applies no encryption at all.
 * 
 * @author danon
 */
public class PlainFileEncryptor extends FileEncryptor {
    
    private final Logger log = Logger.getLogger(PlainFileEncryptor.class);

    public PlainFileEncryptor() {
    }
    

    @Override
    public void encryptFile(File sourceFile, File targetFile, boolean delete) {
        try {
            if(delete) {
                FileUtils.moveFile(sourceFile, targetFile);
            } else {
                FileUtils.copyFile(sourceFile, targetFile);
            }
        } catch(Exception ex) {
            log.error("encryptFile(): Failed to copy/move file file: source = "
                    +sourceFile.getAbsolutePath() + ", target = " 
                    +targetFile.getAbsolutePath(), ex);
            throw new RuntimeException("Failed to copy/move file file", ex);
        }
    }

    @Override
    public void decryptFile(File sourceFile, File targetFile) {
        try {
            FileUtils.copyFile(sourceFile, targetFile);
        } catch(Exception ex) {
            log.error("decryptFile(): Failed to copy/move file file: source = "
                    +sourceFile.getAbsolutePath() + ", target = " 
                    +targetFile.getAbsolutePath(), ex);
            throw new RuntimeException("Failed to copy/move file file", ex);
        }
    }
    
}
