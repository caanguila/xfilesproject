package in.xfiles.core.helpers;

import java.io.File;
import java.security.MessageDigest;
import org.apache.log4j.Logger;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.zip.CRC32;

/**
 *
 * @author 7
 */
public class CryptoHelper {

    private static final Logger log = Logger.getLogger(CryptoHelper.class);
    private static SecretKey secretKey;
    private final static int ENCRYPTION_FILE_BUFFER_SIZE = 1024;

    public static String encryptString(String s, String algo) {
        try {
            MessageDigest md = MessageDigest.getInstance(algo);
            md.update(s.getBytes());
            s = null;
            return toHexString(md.digest());
        } catch (Exception ex) {
            log.error("Couldn't calculate MD5 of string", ex);
            throw new RuntimeException("Couldn't calculate MD5 of string", ex);
        }
    }

    private static String toHexString(byte[] byteData) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String SHA256(String s) {
        return encryptString(s, "SHA-256");
    }

    public static String MD5(String s) {
        return encryptString(s, "MD5");
    }

    private static Cipher getEncryptCipher(String key) {
        try {
            byte[] keyCode = getSecretKeyCode(key);
            if (keyCode == null) {
                log.warn("Can't instatiate cipher, because User's key: " + key);
                return null;
            }
            SecretKeySpec skeySpec = new SecretKeySpec(keyCode, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NOPADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec); // decryptor
            return cipher;

        } catch (NoSuchAlgorithmException ex) {
            log.warn("" + ex.toString());
            return null;
        } catch (NoSuchPaddingException ex) {
            log.warn("" + ex.toString());
            return null;
        } catch (InvalidKeyException ex) {
            log.warn("" + ex.toString());
            return null;
        }
    }

    private static Cipher getDecryptCipher(String key) {
        try {
            byte[] keyCode = getSecretKeyCode(key);
            if (keyCode == null) {
                log.warn("Can't instatiate cipher, because User's key: " + key);
                return null;
            }
            SecretKeySpec skeySpec = new SecretKeySpec(keyCode, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NOPADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec); // decryptor
            return cipher;

        } catch (NoSuchAlgorithmException ex) {
            log.warn("" + ex.toString());
            return null;
        } catch (NoSuchPaddingException ex) {
            log.warn("" + ex.toString());
            return null;
        } catch (InvalidKeyException ex) {
            log.warn("" + ex.toString());
            return null;
        }
    }

    /**
     * This method encrypt input file and return temporary file that stores in
     * ./tmp directory
     *
     * @param file - input file to be encrypted
     * @param type - type of crypt, for example "AES"
     * @param key - String value of user's key. It may has length != 0
     * @return File temp - output temporary file that contains cipher
     * @throws IOException
     */
    public static File enCryptFile(File file, String type, String key) throws IOException {
        File directory = new File("./tmp");
        directory.mkdir();

        //   byte[] keyCode = getSecretKeyCode(key); // this key should be stored in database;
        Cipher cipher = getEncryptCipher(key);
        if (cipher == null) {
            log.warn("Can't encrypt file because Cipher is: " + cipher);
            return null;
        }

        if (type.equalsIgnoreCase("aes")) {

            FileInputStream fis = new FileInputStream(file);
            int fileSize = 0; // should be added to crypt
            try {
                fileSize = fis.available();
            } catch (Exception ex) {
                log.warn(ex.toString());
                return null;
            }
            byte[] f = new byte[ENCRYPTION_FILE_BUFFER_SIZE];

            File temp = File.createTempFile("tmp", "crypt", directory);
            FileOutputStream fos = new FileOutputStream(temp);
            byte[] crypt = null;

            try {
                while (ENCRYPTION_FILE_BUFFER_SIZE < fis.available()) {
                    //     log.debug("file avalable: " + fis.available());
                    fis.read(f);
                    crypt = cipher.doFinal(f);
                    fos.write(crypt);
                }

                //last part of file
                //  Arrays.fill(f, (byte)0);
                f = new byte[ENCRYPTION_FILE_BUFFER_SIZE + 16];
                if (ENCRYPTION_FILE_BUFFER_SIZE >= fis.available()) {
                    byte[] sizeBytes = ByteBuffer.allocate(4).putInt(fileSize).array();
                    int last = fis.available();
                    log.debug("Real file size: " + fileSize + "  bytes: " + toHexString(sizeBytes) + " l: " + sizeBytes.length);
                    log.debug("Last part: " + fis.available());

                    //    log.debug("file avalable: "+fis.available());
                    //f = new byte[fis.available()];
                    fis.read(f);
                    //we will write on last size of the file to the end of pieSize massive
                    f[ENCRYPTION_FILE_BUFFER_SIZE - 4 + 16] = sizeBytes[0];
                    f[ENCRYPTION_FILE_BUFFER_SIZE - 3 + 16] = sizeBytes[1];
                    f[ENCRYPTION_FILE_BUFFER_SIZE - 2 + 16] = sizeBytes[2];
                    f[ENCRYPTION_FILE_BUFFER_SIZE - 1 + 16] = sizeBytes[3];
                    crypt = cipher.doFinal(f);
                    fos.write(crypt);
                }
            } catch (Exception ex) {
                log.warn("Can't encrypt input file:");
                log.warn("Stacktrsce: " + ex);
            }

            fis.close();
            fos.close();
//
//            String mes = toHexString(crypt);
//            log.debug("Crypto:  " + mes);

            return temp;
        } else {

            return null;
        }


    }

    private static byte[] fromHexString(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * This method decrypt input file and return temporary file that stores in
     * ./tmp directory
     *
     * @param file - input file to be encrypted
     * @param type - type of crypt, for example "AES"
     * @param key - String value of user's key. It may has length != 0
     * @return File temp - output temporary file that contains cipher
     * @throws IOException
     */
    public static File deCryptFile(File file, String type, String key) throws IOException {
        File directory = new File("./tmp");
        directory.mkdir();

        // byte[] keyCode = getSecretKeyCode(key); // this key should be stored in database
        Cipher cipher = getDecryptCipher(key);
        FileInputStream fis = new FileInputStream(file);

        if (type.equalsIgnoreCase("aes")) {
            try {
                fis.available();
            } catch (Exception ex) {
                log.warn(ex.toString());
                return null;
            }

            log.debug("Decrypt file avalable: " + fis.available() + "  " + ENCRYPTION_FILE_BUFFER_SIZE);
            byte[] f = new byte[ENCRYPTION_FILE_BUFFER_SIZE];
            File temp = File.createTempFile("tmp", "decrypt", directory);
            FileOutputStream fos = new FileOutputStream(temp);
            byte[] crypt = null;

            try {
                while (ENCRYPTION_FILE_BUFFER_SIZE + 16 < fis.available()) {
                    //  log.debug("file avalable: " + fis.available());
                    fis.read(f);
                    crypt = cipher.doFinal(f);
                    fos.write(crypt);
                }

                //  f = new byte[pieSize+16];           
                if (ENCRYPTION_FILE_BUFFER_SIZE + 16 >= fis.available()) {
                    log.debug("file avalable: " + fis.available());
                    byte[] fileSize = new byte[4];

                    f = new byte[ENCRYPTION_FILE_BUFFER_SIZE + 16];
                    fis.read(f);
                    crypt = cipher.doFinal(f);

                    fileSize[0] = crypt[ENCRYPTION_FILE_BUFFER_SIZE - 4 + 16];
                    fileSize[1] = crypt[ENCRYPTION_FILE_BUFFER_SIZE - 3 + 16];
                    fileSize[2] = crypt[ENCRYPTION_FILE_BUFFER_SIZE - 2 + 16];
                    fileSize[3] = crypt[ENCRYPTION_FILE_BUFFER_SIZE - 1 + 16];

                    //    log.debug("decrypt fileSize: "+toHexString(fileSize));
                    int sizeOfFile = Integer.parseInt(toHexString(fileSize), 16);
                    int lastPart = sizeOfFile % ENCRYPTION_FILE_BUFFER_SIZE;
                    if (lastPart == 0) {
                        lastPart = ENCRYPTION_FILE_BUFFER_SIZE;
                    }
                    //     log.debug("real size: "+sizeOfFile+"  pieSize: "+pieSize);
                    //  log.debug("decrypt last part: "+lastPart);
                    byte[] lastBytePart = new byte[lastPart];

                    for (int i = 0; i < lastPart; i++) {
                        lastBytePart[i] = crypt[i];
                    }

                    fos.write(lastBytePart);
                }
            } catch (Exception ex) {
                log.warn("Can't decrypt input file:");
                log.warn("Stacktrsce: " + ex);
            }


            fis.close();
            fos.close();


            log.debug("deCrypto:  " + crypt);

            return temp;
        }

        return null;
    }

    public static SecretKey getSecretKeyAes() {
        if (secretKey == null) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                kgen.init(128);
                secretKey = kgen.generateKey();
            } catch (NoSuchAlgorithmException ex) {
                log.warn("Can't instatiate key generator: " + ex.toString());
            }
            log.debug("SecrKey:" + toHexString(secretKey.getEncoded()));
        }

        return secretKey;
    }

    /**
     * This method use xor of SecrecCode and code of User
     *
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getSecretKeyCode(String key) {
        if ("".equals(key)) {
            log.warn("User's key is: " + key);
            return null;
        }
        //  log.debug("byte[]"+);
        byte[] code = fromHexString(CommonConstants.SECRET_KEY);
        byte[] keyByte = convertBiteToHalf(fromHexString(MD5(key)));
        if (code.length == 16) {

            for (int i = 0; i < code.length; i++) {
                code[i] = (byte) (code[i] ^ keyByte[i]);
            }

        } else {
            log.debug("AES key length not 128 bit");
        }
        return code;
    }

    private static byte[] convertBiteToHalf(byte[] init) {
        byte[] res = init;

        for (int i = 0; i < res.length; i += 2) {
            res[i / 2] = (byte) (res[i] ^ res[i + 1]);

        }

        return res;
    }

    public static CRC32 getCRC32(File file) throws FileNotFoundException, IOException {

        FileInputStream fis = new FileInputStream(file);
        CRC32 chSumm = new CRC32();

        byte[] f = new byte[ENCRYPTION_FILE_BUFFER_SIZE];
        try {
            while (ENCRYPTION_FILE_BUFFER_SIZE < fis.available()) {

                fis.read(f);
                chSumm.update(f);
            }

            if (ENCRYPTION_FILE_BUFFER_SIZE >= fis.available()) {

                f = new byte[fis.available()];
                fis.read(f);
                chSumm.update(f);
            }
        } catch (Exception ex) {
            log.warn("Can't create CRC32 from input file:");
            log.warn("Stacktrsce: " + ex);
        }
        fis.close();


        return chSumm;

    }
}
