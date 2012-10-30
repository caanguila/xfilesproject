package in.xfiles.core.helpers;

import java.io.File;
import java.security.MessageDigest;
import org.apache.log4j.Logger;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;

/**
 *
 * @author 7
 */
public class CryptoHelper {

    private static final Logger log = Logger.getLogger(CryptoHelper.class);
    private static SecretKey secretKey;
    private final static int pieSize = 1024;

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
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IOException
     * @throws Exception
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

            try {
                fis.available();
            } catch (Exception ex) {
                log.warn(ex.toString());
                return null;
            }
            byte[] f = new byte[pieSize];

            File temp = File.createTempFile("tmp", "crypt", directory);
            FileOutputStream fos = new FileOutputStream(temp);
            byte[] crypt = null;

            try {
                while (pieSize <= fis.available()) {
                    log.debug("file avalable: " + fis.available());
                    fis.read(f);
                    crypt = cipher.doFinal(f);
                    fos.write(crypt);
                }

                //last part of file
                f = new byte[pieSize];
                if (pieSize > fis.available()) {
                    //    log.debug("file avalable: "+fis.available());
                    //f = new byte[fis.available()];
                    fis.read(f);
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

            log.debug("Decrypt file avalable: " + fis.available() + "  " + pieSize);
            byte[] f = new byte[pieSize];
            File temp = File.createTempFile("tmp", "decrypt", directory);
            FileOutputStream fos = new FileOutputStream(temp);
            byte[] crypt = null;

            try {
                while (pieSize <= fis.available()) {
                    log.debug("file avalable: " + fis.available());
                    fis.read(f);
                    crypt = cipher.doFinal(f);
                    fos.write(crypt);
                }
//f = new byte[pieSize];           
                if (pieSize > fis.available()) {
                    //log.debug("file avalable: "+fis.available());
                    f = new byte[fis.available()];
                    fis.read(f);
                    crypt = cipher.doFinal(f);
                    fos.write(crypt);
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

    private static byte[] encrypt_AES(byte[] message, byte[] raw) throws Exception {
        byte[] encrypted_text = null;

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        System.out.println(skeySpec);
        // Instantiate the cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        encrypted_text = cipher.doFinal(message);
        return encrypted_text;
    }

    private static byte[] decrypt_AES(byte[] encrypted_text, byte[] raw) throws Exception {

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] original = cipher.doFinal(encrypted_text);
        return original;
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
        byte[] code = getSecretKeyAes().getEncoded();
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
}
