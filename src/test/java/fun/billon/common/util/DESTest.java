package fun.billon.common.util;

import fun.billon.common.encrypt.DES;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DES加密测试
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class DESTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DESTest.class);

    /**
     * DES密钥
     */
    private static final String DES_KEY = "MhDOtYx6ukoshXX7uepz9NmJUf5rHIp8";

    @Test
    public void testGenerateKey() {
        String key = DES.generateKey(168);
        LOGGER.debug("DES密钥:{}", key);
        Assert.assertNotNull(key);
    }

    @Test
    public void testEncrypt() throws Exception {
        String plain = "测试DES加密算法：“娃哈哈”";
        String encrypted = DES.encrypt(plain, DES_KEY);
        LOGGER.debug("原始数据：{}", plain);
        LOGGER.debug("DES加密：{}", encrypted);
        String decrypted = DES.decrypt(encrypted, DES_KEY);
        LOGGER.debug("DES解密：{}", decrypted);
        Assert.assertEquals(plain, decrypted);
    }

}