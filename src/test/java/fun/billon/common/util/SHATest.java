package fun.billon.common.util;

import fun.billon.common.encrypt.SHA;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SHA加密测试
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class SHATest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SHATest.class);

    @Test
    public void testEncode() {
        String plain = "SHA加密测试";
        String encoded = SHA.encode(plain);
        LOGGER.debug("原始数据：{}", plain);
        LOGGER.debug("SHA加密：{}", encoded);
    }

}