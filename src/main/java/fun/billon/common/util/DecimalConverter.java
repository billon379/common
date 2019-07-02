package fun.billon.common.util;

import fun.billon.common.exception.ParamException;

import java.util.HashMap;
import java.util.Map;

/**
 * 进制转换算法
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class DecimalConverter {

    /**
     * 62进制
     */
    public static final int DECIMAL_62 = 62;

    /**
     * 字符映射表
     */
    private static final char[] DIGIT = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z'};

    /**
     * 字符到十进制数值的映射表
     */
    private static final Map<Character, Integer> DIGIT_DECIMAL_MAPPER = new HashMap<>(62);

    static {
        for (int i = 0; i < DIGIT.length; i++) {
            DIGIT_DECIMAL_MAPPER.put(DIGIT[i], i);
        }
    }

    /**
     * 将十进制数值转换为指定进制的数值(辗转相除取余法)
     *
     * @param decimal 十进制数值
     * @param radix   基数(要转换的进制数,最大是62)
     * @return 十进制数值转换后的数值
     */
    public static String convert(long decimal, int radix) throws ParamException {
        if (radix > DIGIT.length) {
            throw new ParamException("radix最大值为:" + DIGIT.length);
        }

        /*
         * 辗转相除取余,最后将结果反转
         */
        StringBuilder sb = new StringBuilder();
        while (decimal >= radix) {
            sb.append(DIGIT[(int) (decimal % radix)]);
            decimal = decimal / radix;
        }
        sb.append(DIGIT[(int) (decimal)]);
        return sb.reverse().toString();
    }

    /**
     * 将指定进制的数值转换为十进制数值
     *
     * @param decimal 十进制数值
     * @param radix   基数(数值的进制数,最大是62)
     * @return 转换为十进制后的数值
     */
    public static long reverse(String decimal, int radix) throws ParamException {
        if (radix > DIGIT.length) {
            throw new ParamException("radix最大值为:" + DIGIT.length);
        }
        String decimalReverse = new StringBuilder(decimal).reverse().toString();
        long result = 0;
        char[] arry = decimalReverse.toCharArray();
        for (int i = 0; i < arry.length; i++) {
            if (DIGIT_DECIMAL_MAPPER.get(arry[i]) == null) {
                throw new ParamException("decimal参数非法");
            }
            result += DIGIT_DECIMAL_MAPPER.get(arry[i]) * Math.pow(radix, i);
        }
        return result;
    }

}