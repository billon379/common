package fun.billon.common.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 进制转换算法测试类
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class DecimalConverterTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecimalConverterTest.class);

    @Test
    public void testConvert() throws Exception {
        long decimal = 100001;
        int radix = 62;
        String convertResult = DecimalConverter.convert(decimal, radix);
        LOGGER.debug("十进制{}转换为{}进制:{}", decimal, radix, convertResult);
        long reverseResult = DecimalConverter.reverse(convertResult, radix);
        LOGGER.debug("{}进制{}转换为十进制:{}", radix, convertResult, reverseResult);
    }

}