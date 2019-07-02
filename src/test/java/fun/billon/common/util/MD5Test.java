package fun.billon.common.util;

import fun.billon.common.encrypt.MD5;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MD5加密测试
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class MD5Test {

    private static final Logger LOGGER = LoggerFactory.getLogger(MD5Test.class);

    @Test
    public void testEncode() {
        String plain = "MD5加密测试";
        String encoded = MD5.encode(plain);
        LOGGER.debug("原始数据：{}", plain);
        LOGGER.debug("MD5加密：{}", encoded);
    }

}