package com.regionalTourism.common;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Formatter;

/**
 * @author jlabarca
 */
public class AuthCommons {
    protected static boolean debug = false;
    protected static String projectName;
    protected static String version = "0.0.1"; //checkscore_base version
    protected static String basePath = ".";
    protected static String logName;
    protected static boolean heartbeat = false;
    protected static Integer heartbeat_interval = 5;
    private static final long cd_key_constant = 350000000;

    public static final Logger log = LoggerFactory.getLogger(AuthCommons.class);

    public static void setProjectName(String name){
        projectName = name;
    }
    public static String getProjectName(){
        return projectName;
    }

    /**
     * Prevent instantiation
     */
    private AuthCommons() {}
    public static String getCdKey(String hostname, String mac, String timestamp, String productName) {
        String result = null;
        if (hostname==null||hostname.isEmpty()||mac==null||mac.isEmpty()||
                timestamp==null||timestamp.isEmpty()||productName==null||productName.isEmpty()){
            log.error("getCdKey: one of the arguments is null or empty, exiting...");
            return result;
        }
        String hash_value = SHA1( hostname + mac + timestamp);
        long offset = cd_key_constant - asciiValue(hash_value);
        String cdkey_original = productName + ";" + timestamp + ";" + mac + ";" + hostname + ";" + offset;

        String key = CreateKey(String.valueOf(cd_key_constant), hostname);
        result = encryptNumber(cdkey_original, key).replaceAll("(?:\\\\r\\\\n|\\\\n\\\\r|\\\\n|\\\\r)", "");

        return result.replaceAll("\n", "").replaceAll("\r", "");
    }

    //new methods
    public static String getOSName(){
        return System.getProperty("os.name");
    }

    public static String getMACAddress(){
        String result = null;
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while(networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] mac = network.getHardwareAddress();
                if(mac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    result = sb.toString();
                    break;
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
            result = System.getProperty("user.dir");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            result = System.getProperty("user.dir");
        }
        return result;
    }

    /**
     * Realiza la funcion HASH (SHA1) a una cadena
     * @param cadena cadena a codificar
     * @return String - cadena codificada
     */
    public static String SHA1(String cadena){
        String ret = null;
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(cadena.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }

            ret = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            log.error("Error al generar la encriptación");
            ret = null;
        }
        return ret;
    }

    public static long asciiValue(String s){
        long sum = 0;

        for(int i = 0; i < s.length(); i += 4){
            int inter_sum = 1;
            String sub_string = s.substring(i, i+4);
            for( int j = 0; j < sub_string.length(); j++){
                char c = sub_string.charAt(j);
                int k = (int) c;
                inter_sum *= k;
            }
            sum += inter_sum;
        }
        return sum;
    }

    public static String CreateKey(String pass, String email){
        String email_encript = email + DigestUtils.shaHex("hola");
        String pass_encript = DigestUtils.shaHex(pass);
        String key_mac = HMAC( email_encript, pass_encript);
        return key_mac.substring(0,16);
    }

    public static String HMAC(String data, String key){
        String ret = null;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] hmac = mac.doFinal(data.getBytes());
            Formatter formatter = new Formatter();
            for (byte b : hmac) {
                formatter.format("%02x", b);
            }
            ret = formatter.toString();
        } catch (NoSuchAlgorithmException ex) {

        } catch (InvalidKeyException ex) {

        }
        return ret;
    }

    /**
     * Encripta un String mediante una llave anteriormente definida.
     * @param value cadena a codificar
     * @param key Llave para la codificacion
     * @return String - valor de la cadena codificada.
     */
    public static String encryptNumber(String value, String key){
        String ret = null;
        try {
            byte[] raw = key.getBytes(Charset.forName("US-ASCII"));
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec,new IvParameterSpec(new byte[16]));
            byte[] enc  = cipher.doFinal(value.getBytes(Charset.forName("US-ASCII")));

            //ret = new sun.misc.BASE64Encoder().encode(enc);
            ret = new String(Base64.getUrlEncoder().encode(enc));
        } catch (NoSuchAlgorithmException ex) {

        } catch (NoSuchPaddingException ex) {

        } catch (InvalidKeyException ex) {

        } catch (InvalidAlgorithmParameterException ex) {

        } catch (IllegalBlockSizeException ex) {

        } catch (BadPaddingException ex) {

        } catch (Exception ex) {
        }
        return ret;
    }
}