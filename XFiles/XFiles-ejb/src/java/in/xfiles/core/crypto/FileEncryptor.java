package in.xfiles.core.crypto;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for that provides access to supported encryption methods.
 *
 * @author danon
 */
public abstract class FileEncryptor {
    
    private String key;
    
    /**
     * Encrypts source file and writes results to target file.
     * @param sourceFile source file to be encrypted
     * @param targetFile target file
     * @param delete if it is true, it means that source file should be deleted
     */
    public abstract void encryptFile(File sourceFile, File targetFile, boolean delete);
    
    /**
     * Decrypts previously encrypted file.
     * @param sourceFile encrypted source file
     * @param targetFile target file
     */
    public abstract void decryptFile(File sourceFile, File targetFile);
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public static FileEncryptor getEncryptor(String engine) throws InstantiationException, IllegalAccessException {
        Class<? extends FileEncryptor> clazz = engines.get(engine);
        if(clazz == null)
            throw new UnsupportedOperationException("Unsupported encryption engine: "+engine);
        return clazz.newInstance();
    }
    
    public static List<String> getSupportedEngines() {
        return new ArrayList<String>(engines.keySet());
    }
    
    private static final Map<String, Class<? extends FileEncryptor>> engines = new HashMap<String, Class<? extends FileEncryptor>>() {
        {
            put("plain", PlainFileEncryptor.class);
            put("AES", AESFileEncryptor.class);
        }
    };
}
