package fun.whmy.adminfast.admin.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RSAUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtils.class);
    private static final String ALGORITHOM = "RSA";
    private static final String RSA_PAIR_FILENAME = "/__RSA_PAIR.txt";
    private static final int KEY_SIZE = 1024;
    private static final Provider DEFAULT_PROVIDER = new BouncyCastleProvider();
    private static KeyPairGenerator keyPairGen = null;
    private static KeyFactory keyFactory = null;
    private static KeyPair oneKeyPair = null;
    private static File rsaPairFile = null;
    private static Map<String, KeyPair> keyPairMap = new ConcurrentHashMap<String, KeyPair>();

    static {
        try {
            keyPairGen = KeyPairGenerator.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
            keyFactory = KeyFactory.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    private static synchronized KeyPair generateKeyPair() {
        try {
            keyPairGen.initialize(KEY_SIZE,
                    new SecureRandom(DateUtil.format(new Date(), "yyyyMMdd HH:mm:ss:SSS").getBytes()));

            oneKeyPair = keyPairGen.generateKeyPair();

            return oneKeyPair;
        } catch (InvalidParameterException ex) {
            LOGGER.error("KeyPairGenerator does not support a key length of 1024.", ex);
        } catch (NullPointerException ex) {
            LOGGER.error("RSAUtils#KEY_PAIR_GEN is null, can not generate KeyPairGenerator instance.", ex);
        }
        return null;
    }

    @SuppressWarnings("unused")
    private static String getRSAPairFilePath() {
        String urlPath = RSAUtils.class.getResource("/").getPath();
        return new File(urlPath).getParent() + RSA_PAIR_FILENAME;
    }

    private static boolean isCreateKeyPairFile() {
        boolean createNewKeyPair = false;
        if ((!rsaPairFile.exists()) || (rsaPairFile.isDirectory())) {
            createNewKeyPair = true;
        }
        return createNewKeyPair;
    }

    @SuppressWarnings({"unused", "deprecation"})
    private static void saveKeyPair(KeyPair keyPair) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = FileUtils.openOutputStream(rsaPairFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(keyPair);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(fos);
        }
    }

    public static KeyPair getKeyPair() {
        if (isCreateKeyPairFile()) {
            return generateKeyPair();
        }
        if (oneKeyPair != null) {
            return oneKeyPair;
        }
        return readKeyPair();
    }

    @SuppressWarnings("deprecation")
    private static KeyPair readKeyPair() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = FileUtils.openInputStream(rsaPairFile);
            ois = new ObjectInputStream(fis);
            oneKeyPair = (KeyPair) ois.readObject();
            return oneKeyPair;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(fis);
        }
        return null;
    }

    public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) {
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
        try {
            return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException ex) {
            LOGGER.error("RSAPublicKeySpec is unavailable.", ex);
        } catch (NullPointerException ex) {
            LOGGER.error("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
        }
        return null;
    }

    public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) {
        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus),
                new BigInteger(privateExponent));
        try {
            return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
        } catch (InvalidKeySpecException ex) {
            LOGGER.error("RSAPrivateKeySpec is unavailable.", ex);
        } catch (NullPointerException ex) {
            LOGGER.error("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
        }
        return null;
    }

    public static RSAPrivateKey getRSAPrivateKey(String hexModulus, String hexPrivateExponent) {
        if ((StrUtil.isBlank(hexModulus)) || (StrUtil.isBlank(hexPrivateExponent))) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(
                        "hexModulus and hexPrivateExponent cannot be empty. RSAPrivateKey value is null to return.");
            }
            return null;
        }
        byte[] modulus = null;
        byte[] privateExponent = null;
        try {
            modulus = Hex.decodeHex(hexModulus.toCharArray());
            privateExponent = Hex.decodeHex(hexPrivateExponent.toCharArray());
        } catch (DecoderException ex) {
            LOGGER.error("hexModulus or hexPrivateExponent value is invalid. return null(RSAPrivateKey).");
        }
        if ((modulus != null) && (privateExponent != null)) {
            return generateRSAPrivateKey(modulus, privateExponent);
        }
        return null;
    }

    public static RSAPublicKey getRSAPublidKey(String hexModulus, String hexPublicExponent) {
        if ((StrUtil.isBlank(hexModulus)) || (StrUtil.isBlank(hexPublicExponent))) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("hexModulus and hexPublicExponent cannot be empty. return null(RSAPublicKey).");
            }
            return null;
        }
        byte[] modulus = null;
        byte[] publicExponent = null;
        try {
            modulus = Hex.decodeHex(hexModulus.toCharArray());
            publicExponent = Hex.decodeHex(hexPublicExponent.toCharArray());
        } catch (DecoderException ex) {
            LOGGER.error("hexModulus or hexPublicExponent value is invalid. return null(RSAPublicKey).");
        }
        if ((modulus != null) && (publicExponent != null)) {
            return generateRSAPublicKey(modulus, publicExponent);
        }
        return null;
    }

    public static byte[] encrypt(PublicKey publicKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
        ci.init(1, publicKey);
        return ci.doFinal(data);
    }

    public static byte[] decrypt(PrivateKey privateKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
        ci.init(2, privateKey);
        return ci.doFinal(data);
    }

    public static String encryptString(PublicKey publicKey, String plaintext) {
        if ((publicKey == null) || (plaintext == null)) {
            return null;
        }
        byte[] data = plaintext.getBytes();
        try {
            byte[] en_data = encrypt(publicKey, data);
            return new String(Hex.encodeHex(en_data));
        } catch (Exception ex) {
            LOGGER.error(ex.getCause().getMessage());
        }
        return null;
    }

    public static String encryptString(String plaintext) {
        if (plaintext == null) {
            return null;
        }
        byte[] data = plaintext.getBytes();
        KeyPair keyPair = getKeyPair();
        try {
            byte[] en_data = encrypt((RSAPublicKey) keyPair.getPublic(), data);
            return new String(Hex.encodeHex(en_data));
        } catch (NullPointerException ex) {
            LOGGER.error("keyPair cannot be null.");
        } catch (Exception ex) {
            LOGGER.error(ex.getCause().getMessage());
        }
        return null;
    }

    public static String decryptString(PrivateKey privateKey, String encrypttext) {
        if ((privateKey == null) || (StrUtil.isBlank(encrypttext))) {
            return null;
        }
        try {
            byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
            byte[] data = decrypt(privateKey, en_data);
            return new String(data);
        } catch (Exception ex) {
            LOGGER.error(String.format("\"%s\" Decryption failed. Cause: %s",
                    new Object[]{encrypttext, ex.getCause().getMessage()}));
        }
        return null;
    }

    public static String decryptString(String encrypttext, String module) {
        if (StrUtil.isBlank(encrypttext)) {
            return null;
        }
        KeyPair keyPair = (KeyPair) keyPairMap.get(module);
        try {
            byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
            byte[] data = decrypt((RSAPrivateKey) keyPair.getPrivate(), en_data);
            return new String(data);
        } catch (NullPointerException ex) {
            LOGGER.error("keyPair cannot be null.");
        } catch (Exception ex) {
            LOGGER.error(String.format("\"%s\" Decryption failed. Cause: %s",
                    new Object[]{encrypttext, ex.getMessage()}));
        }
        return null;
    }

    public static String decryptString(String encrypttext, KeyPair keyPair) {
        if (StrUtil.isBlank(encrypttext)) {
            return null;
        }
        try {
            byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
            byte[] data = decrypt((RSAPrivateKey) keyPair.getPrivate(), en_data);
            return new String(data);
        } catch (NullPointerException ex) {
            LOGGER.error("keyPair cannot be null.");
        } catch (Exception ex) {
            LOGGER.error(String.format("\"%s\" Decryption failed. Cause: %s",
                    new Object[]{encrypttext, ex.getMessage()}));
        }
        return null;
    }

    public static String decryptString(String encrypttext) {
        if (StrUtil.isBlank(encrypttext)) {
            return null;
        }
        KeyPair keyPair = getKeyPair();
        try {
            byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
            byte[] data = decrypt((RSAPrivateKey) keyPair.getPrivate(), en_data);
            return new String(data);
        } catch (NullPointerException ex) {
            LOGGER.error("keyPair cannot be null.");
        } catch (Exception ex) {
            LOGGER.error(String.format("\"%s\" Decryption failed. Cause: %s",
                    new Object[]{encrypttext, ex.getMessage()}));
        }
        return null;
    }

    public static String decryptStringByJs(String encrypttext, String module) {
        String text = decryptString(encrypttext, module);
        if (text == null) {
            return null;
        }
        return StrUtil.reverse(text);
    }

    public static String decryptStringByJs(String encrypttext, KeyPair keyPair) {
        String text = decryptString(encrypttext, keyPair);
        if (text == null) {
            return null;
        }
        return StrUtil.reverse(text);
    }

    public static RSAPublicKey getDefaultPublicKey() {
        KeyPair keyPair = getKeyPair();
        if (keyPair != null) {
            return (RSAPublicKey) keyPair.getPublic();
        }
        return null;
    }

    public static RSAPrivateKey getDefaultPrivateKey() {
        KeyPair keyPair = getKeyPair();
        if (keyPair != null) {
            return (RSAPrivateKey) keyPair.getPrivate();
        }
        return null;
    }

    public static PublicKeyMap getPublicKeyMap() {
        PublicKeyMap publicKeyMap = new PublicKeyMap();
        KeyPair keyPair = generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        publicKeyMap.setModulus(new String(Hex.encodeHex(rsaPublicKey.getModulus().toByteArray())));
        publicKeyMap.setExponent(new String(Hex.encodeHex(rsaPublicKey.getPublicExponent().toByteArray())));

        keyPairMap.put(publicKeyMap.getModulus(), keyPair);
        return publicKeyMap;
    }

    public static KeyPair getKeyPairByModulus(String modulus) {
        KeyPair keyPair = (KeyPair) keyPairMap.get(modulus);
        //防止内存溢出
        keyPairMap.remove(modulus);
        return keyPair;
    }


    public static void clearKeyPairMap() {
        keyPairMap.clear();
    }
}
