package fun.billon.common.util;

import java.math.BigDecimal;

/**
 * 浮点数计算工具类
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class DecimalUtils {

    /**
     * 浮点数相减
     *
     * @param f1 被减数
     * @param f2 减数
     * @return 结果
     */
    public static float sub(float f1, float f2) {
        BigDecimal b1 = new BigDecimal(Float.toString(f1));
        BigDecimal b2 = new BigDecimal(Float.toString(f2));
        return b1.subtract(b2).floatValue();
    }

    /**
     * 浮点数相减
     *
     * @param d1 被减数
     * @param d2 减数
     * @return 结果
     */
    public static double sub(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 浮点数相加
     *
     * @param f1 加数
     * @param f2 被加数
     * @return 结果
     */
    public static float add(float f1, float f2) {
        BigDecimal b1 = new BigDecimal(Float.toString(f1));
        BigDecimal b2 = new BigDecimal(Float.toString(f2));
        return b1.add(b2).floatValue();
    }

    /**
     * 浮点数相加
     *
     * @param d1 加数
     * @param d2 被加数
     * @return 结果
     */
    public static double add(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 浮点数相乘
     *
     * @param f1 被乘数
     * @param f2 乘数
     * @return 结果
     */
    public static float mul(float f1, float f2) {
        BigDecimal b1 = new BigDecimal(Float.toString(f1));
        BigDecimal b2 = new BigDecimal(Float.toString(f2));
        return b1.multiply(b2).floatValue();
    }

    /**
     * 浮点数相乘
     *
     * @param d1 被乘数
     * @param d2 乘数
     * @return 结果
     */
    public static double mul(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.multiply(b2).doubleValue();
    }

}
