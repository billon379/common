package fun.billon.common.util;

import fun.billon.common.encrypt.RSA;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * RSA加密测试
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class RSATest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSATest.class);

    /**
     * 公钥Base64编码
     */
    private static final String RSA_PUBLIC = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHbVuGAOSN0yi24BQcizkNpTzFzZDrPNzXamCtObzbQeO0Y1R2zYi9alBWQPhOUTMmLkI+t0Lye7wfRQ/AYyE0CAwEAAQ==";

    /**
     * 私钥Base64编码
     */
    private static final String RSA_PRIVATE = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAodtW4YA5I3TKLbgFByLOQ2lPMXNkOs83NdqYK05vNtB47RjVHbNiL1qUFZA+E5RMyYuQj63QvJ7vB9FD8BjITQIDAQABAkBDbD8QyH0EzA28Zb+SbWlGo4Odsde6t77j7B8fSxkA18JMtnysdKABdB4F6AuOSQbnA1SYcTfngZTFiYkyGUkBAiEA9Memk9NnvFznlG9QwF7jFebjSXafxrJZYK+c1X3k/3ECIQCpRqJw+WngNE84RO4LgY/R8qceXXRcZ0fN0XzfnVognQIhALye4GDHJgfDO3xpLrueIrTs3zXr4AEXy8GycVMeGQkxAiAQFMfF18v5Ir8L53B941cYaOb7eYFebeliYi5sx/QR+QIhAM3aCdNh1uBQS084QMnWNVUNcIwHWh9iQhwZVjvFCUj8";

    @Test
    public void testGenerateKeyPair() {
        Map<String, String> keyPair = RSA.generateKeyPair(1024);
        LOGGER.debug("公钥Base64编码:{}", keyPair.get("publicKey"));
        LOGGER.debug("私钥Base64编码:{}", keyPair.get("privateKey"));
        Assert.assertEquals(keyPair.size(), 2);
    }

    @Test
    public void testSign() throws Exception {
        String plain = "测试RSA签名算法：“娃哈哈”";
        String signature = RSA.sign(plain, RSA_PRIVATE);
        LOGGER.debug("原始数据：{}", plain);
        LOGGER.debug("RSA私钥签名：{}", signature);
        boolean result = RSA.verify(plain, signature, RSA_PUBLIC);
        LOGGER.debug("RSA公钥验签：{}", result);
        Assert.assertTrue(result);
    }

    @Test
    public void testEncrypt() throws Exception {
        String plain = "测试RSA加密算法：“娃哈哈”";
        String encrypted = RSA.encrypt(plain, RSA_PUBLIC);
        LOGGER.debug("原始数据：{}", plain);
        LOGGER.debug("RSA公钥加密：{}", encrypted);
        String decrypted = RSA.decrypt(encrypted, RSA_PRIVATE);
        LOGGER.debug("RSA私钥解密：{}", decrypted);
        Assert.assertEquals(plain, decrypted);
    }

}