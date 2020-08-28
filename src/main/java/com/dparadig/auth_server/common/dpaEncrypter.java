package com.dparadig.auth_server.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class dpaEncrypter {
    protected final Logger log = LoggerFactory.getLogger(dpaEncrypter.class);
    SecretKey secretKey;

    public static int AES_KEY_SIZE = 128 ;
    public static int IV_SIZE = 96 / 8;
    public static int TAG_BIT_LENGTH = 128 ;
    public static String ALGO_TRANSFORMATION_STRING = "AES/GCM/PKCS5Padding" ;

    int iterationCount = 512;

    public dpaEncrypter(String password) {
        try {
            // Generating Key
            secretKey = null ;
            /*try {
                KeyGenerator keygen = KeyGenerator.getInstance("AES") ; // Specifying algorithm key will be used for
                keygen.init(AES_KEY_SIZE) ; // Specifying Key size to be used, Note: This would need JCE Unlimited Strength to be installed explicitly
                secretKey = keygen.generateKey() ;
            } catch(Exception noSuchAlgoExc) {
                log.info("Key being request is for AES algorithm, but this cryptographic algorithm is not available in the environment "  + noSuchAlgoExc) ;
            }*/

            /* Derive the key, given password and salt. */
            byte[] salt = new String("asd987uj").getBytes("UTF-8");
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, AES_KEY_SIZE);
            SecretKey tmp = factory.generateSecret(spec);
            secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (Exception e){
            log.error("starting dpaEncrypter: exception: "+e.getMessage());
        }
    }

    public String encryptString(String data){
        log.debug("RMR - encryptString: encripting: "+data);
        String ret = "";
        try {
            //init cipher
            byte iv[] = new byte[IV_SIZE];
            SecureRandom secRandom = new SecureRandom() ;
            secRandom.nextBytes(iv); // self-seeded randomizer to generate IV
            log.debug("RMR - encryptString: ivLength: "+iv.length);
            final Cipher cipher = Cipher.getInstance(ALGO_TRANSFORMATION_STRING);
            //cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_BIT_LENGTH, iv); //128 bit auth tag length
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec, new SecureRandom());
            //encrypt
            byte[] cipherText = cipher.doFinal(data.trim().getBytes("UTF-8"));
            ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + cipherText.length);
            byteBuffer.putInt(iv.length);
            byteBuffer.put(iv);
            byteBuffer.put(cipherText);
            byte[] cipherMessage = Base64.getUrlEncoder().encode(byteBuffer.array());
            ret = new String(cipherMessage, "UTF-8");
            log.debug("RMR - encryptString: encripted to base64: "+ ret);
        } catch (Exception e){
            log.error("encryptString: exception: "+e.getMessage());
        }
        return ret;
    }

    public String decryptString(String data){
        log.debug("RMR - decryptString: decripting: "+data);
        String ret = "";
        try {
            byte[] cipherMessage = Base64.getUrlDecoder().decode(data.trim());
            log.debug("RMR - decryptString: input data: "+ new String(cipherMessage, "UTF-8"));
            ByteBuffer byteBuffer = ByteBuffer.wrap(cipherMessage);
            int ivLength = byteBuffer.getInt();
            log.debug("RMR - decryptString: ivLength: "+ivLength);
            if(ivLength < 12 || ivLength >= 16) { // check input parameter
                throw new IllegalArgumentException("invalid iv length");
            }
            byte[] iv = new byte[ivLength];
            byteBuffer.get(iv);
            byte[] cipherText = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherText);

            final Cipher cipher = Cipher.getInstance(ALGO_TRANSFORMATION_STRING);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));

            byte[] plainText= cipher.doFinal(cipherText);
            ret = new String(plainText);
            log.debug("RMR - decryptString: original msg is: "+ ret);
        } catch (Exception e){
            log.error("decryptString: exception: "+e.getMessage());
        }
        return ret;
    }

}

