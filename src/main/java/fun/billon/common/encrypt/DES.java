package fun.billon.common.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * DES加密工具
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class DES {

    /**
     * 加密算法(DESede)
     */
    private static final String ENCRYPT_ALGORITHM_DES = "DESede";

    /**
     * 加密算法/工作模式/填充方式
     */
    private static final String CIPHER_ALGORITHM_DES_ECB_PKCS5 = "DESede/ECB/PKCS5Padding";

    /**
     * 字符集
     */
    private static final String CHARSET = "UTF-8";

    /**
     * 生成Base64编码的DES密钥
     * 1)生成DES密钥
     * 2)将密钥进行Base64编码
     *
     * @param keySize 密钥长度(112,168)
     * @return Base64编码的DES密钥
     */
    public static String generateKey(int keySize) {
        // 秘钥生成器
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance(ENCRYPT_ALGORITHM_DES);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // 设置密钥长度
        keyGen.init(keySize);
        // 生成密钥
        SecretKey secretKey = keyGen.generateKey();
        // 将密钥进行Base64编码
        return Base64.encodeBase64String(secretKey.getEncoded());
    }

    /**
     * 使用DES算法,对数据进行加密
     * 1)将DES密钥使用Base64解码
     * 2)加密数据
     * 3)对加密后的数据使用Base64编码
     *
     * @param plain              明文
     * @param desSecretKeyBase64 Base64编码的DES密钥
     * @return 经过DES加密后使用Base64编码的数据
     */
    public static String encrypt(String plain, String desSecretKeyBase64) throws Exception {
        // Cipher完成加密或者解密
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_DES_ECB_PKCS5);
        // 根基密钥,对Cipher进行初始化ENCRYPT_MODE(加密)，DECRYPT_MODE(解密)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(desSecretKeyBase64));
        // 加密
        byte[] encrypted = cipher.doFinal(plain.getBytes(CHARSET));
        // 将加密后的数据使用Base64编码
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * 使用DES数据解密
     * 1)将DES密钥使用Base64解码
     * 2)将加密的数据使用Base64解码
     * 3)使用DES解密
     *
     * @param encryptedBase64    经过DES加密后使用Base64编码的数据
     * @param desSecretKeyBase64 Base64编码的DES密钥
     * @return DES解密后的明文
     */
    public static String decrypt(String encryptedBase64, String desSecretKeyBase64) throws Exception {
        // Cipher完成加密或者解密
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_DES_ECB_PKCS5);
        // 根基密钥，对Cipher进行初始化ENCRYPT_MODE(加密)，DECRYPT_MODE(解密)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(desSecretKeyBase64));
        // 将加密的数据使用Base64解码,然后使用DES解密
        byte[] plainBytes = cipher.doFinal(Base64.decodeBase64(encryptedBase64.getBytes(CHARSET)));
        return new String(plainBytes, CHARSET);
    }

    /**
     * 获取DES密钥
     * 1)将DES密钥使用Base64解码
     * 2)恢复DES密钥
     *
     * @param desSecretKeyBase64 使用Base64编码后的DES密钥
     * @return DES密钥
     * @throws Exception 异常
     */
    private static SecretKey getSecretKey(String desSecretKeyBase64) throws Exception {
        // 恢复密钥
        return new SecretKeySpec(Base64.decodeBase64(desSecretKeyBase64.getBytes(CHARSET)), ENCRYPT_ALGORITHM_DES);
    }

}