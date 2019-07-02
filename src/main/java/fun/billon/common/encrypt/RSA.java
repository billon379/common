package fun.billon.common.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加密工具
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class RSA {

    /**
     * 加密算法(RSA)
     */
    private static final String ENCRYPT_ALGORITHM_RSA = "RSA";

    /**
     * 签名算法(SHA1WithRSA)
     */
    private final static String SIGNATURE_ALGORITHM_SHA1_WITH_RSA = "SHA1WithRSA";

    /**
     * 字符集
     */
    private static final String CHARSET = "UTF-8";

    /**
     * 生成Base64编码的RSA密钥对
     * 1)生成密钥对
     * 2)对密钥进行Base64编码
     *
     * @param keySize 密钥长度
     * @return Base64编码的RSA密钥对
     */
    public static Map<String, String> generateKeyPair(int keySize) {
        // 为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance(ENCRYPT_ALGORITHM_RSA);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // 初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        // 生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        // 得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyBase64 = Base64.encodeBase64String(publicKey.getEncoded());
        // 得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyBase64 = Base64.encodeBase64String(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<>(2);
        keyPairMap.put("publicKey", publicKeyBase64);
        keyPairMap.put("privateKey", privateKeyBase64);

        return keyPairMap;
    }

    /**
     * RSA签名,使用私钥对数据签名
     * 1)将私钥使用Base64解码
     * 2)使用SHA1WithRSA对数据进行签名
     * 3)将签名后的数据使用Base64编码
     *
     * @param plain            原始数据
     * @param privateKeyBase64 Base64编码后的私钥
     * @return Base64编码后的签名数据
     * @throws Exception 异常
     */
    public static String sign(String plain, String privateKeyBase64) throws Exception {
        // 初始化签名算法
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM_SHA1_WITH_RSA);
        sig.initSign(getPrivateKey(privateKeyBase64));
        sig.update(plain.getBytes(CHARSET));
        // 对原始数据进行签名
        byte[] signature = sig.sign();
        // 将签名后的数据进行Base64编码
        return Base64.encodeBase64String(signature);
    }

    /**
     * RSA验签,使用公钥验签
     * 1)将公钥使用Base64解码
     * 2)将签名使用Base64解码
     * 3)验签
     *
     * @param plain           原始数据
     * @param signatureBase64 Base64编码后的签名数据
     * @param publicKeyBase64 Base64编码后的公钥
     * @return 验签是否通过
     * @throws Exception 异常
     */
    public static boolean verify(String plain, String signatureBase64, String publicKeyBase64) throws Exception {
        // 初始化签名算法
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM_SHA1_WITH_RSA);
        sig.initVerify(getPublicKey(publicKeyBase64));
        sig.update(plain.getBytes(CHARSET));
        // 将签名使用Base64解码
        byte[] signatureBase64Decoded = Base64.decodeBase64(signatureBase64.getBytes(CHARSET));
        // 验签
        return sig.verify(signatureBase64Decoded);
    }

    /**
     * RSA加密,使用公钥加密
     * 1)将公钥使用Base64解码
     * 2)使用公钥对数据加密
     * 3)将加密后的数据使用Base64编码
     *
     * @param plain           原始数据
     * @param publicKeyBase64 Base64编码后的公钥
     * @return 经过RSA公钥加密后使用Base64编码的数据
     */
    public static String encrypt(String plain, String publicKeyBase64) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKeyBase64));
        byte[] encrypted = cipher.doFinal(plain.getBytes(CHARSET));
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * RSA解密,使用私钥解密
     * 1)将私钥使用Base64解码
     * 2)将加密数据使用Base64解码
     * 3)使用私钥对数据解密
     *
     * @param encryptedBase64  Base64编码后密文
     * @param privateKeyBase64 Base64编码后的私钥
     * @return RSA解密后的明文
     * @throws Exception 异常
     */
    public static String decrypt(String encryptedBase64, String privateKeyBase64) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM_RSA);
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKeyBase64));
        byte[] bytes = cipher.doFinal(Base64.decodeBase64(encryptedBase64.getBytes(CHARSET)));
        return new String(bytes, CHARSET);
    }

    /**
     * 获取公钥,将公钥使用Base64解码
     *
     * @param publicKeyBase64 Base64编码后的公钥
     * @return Base64解码后的公钥
     * @throws Exception 异常
     */
    private static PublicKey getPublicKey(String publicKeyBase64) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPT_ALGORITHM_RSA);
        // 将公钥使用Base64解码
        byte[] publicKeyBase64Decoded = Base64.decodeBase64(publicKeyBase64.getBytes(CHARSET));
        // 获取公钥
        return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBase64Decoded));
    }

    /**
     * 获取私钥,将私钥使用Base64解码
     *
     * @param privateKeyBase64 Base64编码后的私钥
     * @return Base64解码后的私钥
     * @throws Exception 异常
     */
    private static PrivateKey getPrivateKey(String privateKeyBase64) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPT_ALGORITHM_RSA);
        // 将私钥使用Base64解码
        byte[] privateKeyBase64Decoded = Base64.decodeBase64(privateKeyBase64.getBytes());
        // 获取私钥
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBase64Decoded));
    }

}