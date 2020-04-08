package com.ck.ckeditor_mathtype.utils;

import java.math.BigDecimal;

/**
 * @author lix
 * @date 2020/3/23 14:30
 * @description
 */
public class DoubleUtils {
    // 默认除法运算精度
    private static final Integer DEF_DIV_SCALE = 2;

    /**
     * 提供精确的加法运算。
     *
     * @param value1
     *            被加数
     * @param value2
     *            加数
     * @return 两个参数的和
     */
    public static Double add(Number value1, Number value2) {
        return add(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 提供精确的加法运算。
     *
     * @param value1
     *            被加数
     * @param value2
     *            加数
     * @param scale
     *            表示需要精确到小数点以后几位。
     * @return 两个参数的和
     */
    public static Double add(Number value1, Number value2, Integer scale) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(value2.doubleValue()));
        BigDecimal add = b1.add(b2);
        BigDecimal setScale = add.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return setScale.doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param value1
     *            被减数
     * @param value2
     *            减数
     * @return
     */
    public static Double sub(Number value1, Number value2) {
        return sub(value1, value2, DEF_DIV_SCALE);
    }

    /**
     *
     * @param value1
     *            被减数
     * @param value2
     *            减数
     * @param scale
     *            表示需要精确到小数点以后几位。
     * @return 两个参数的差
     */
    public static Double sub(Number value1, Number value2, Integer scale) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(value2.doubleValue()));
        BigDecimal subtract = b1.subtract(b2);
        BigDecimal setScale = subtract.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return setScale.doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param value1
     *            被乘数
     * @param value2
     *            乘数
     * @return 两个参数的积
     */
    public static Double mul(Number value1, Number value2) {
        return mul(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param value1
     *            被乘数
     * @param value2
     *            乘数
     * @param scale
     *            表示需要精确到小数点以后几位。
     * @return 两个参数的积
     */
    public static Double mul(Number value1, Number value2, Integer scale) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(value2.doubleValue()));
        BigDecimal multiply = b1.multiply(b2);
        BigDecimal setScale = multiply.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return setScale.doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param value1
     *            被乘数
     * @param value2
     *            乘数
     * @param scale
     *            表示需要精确到小数点以后几位。
     *
     * @return 两个参数的积
     */
    public static Double mul(Number value1, Number value2, int scale, int roundingMode) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(value2.doubleValue()));
        BigDecimal multiply = b1.multiply(b2);
        BigDecimal setScale = multiply.setScale(scale, roundingMode);
        return setScale.doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点以后2位，以后的数字四舍五入。
     *
     * @param dividend
     *            被除数
     * @param divisor
     *            除数
     * @return 两个参数的商
     */
    public static Double div(Number dividend, Number divisor) {
        return DoubleUtils.div(dividend, divisor, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param dividend
     *            被除数
     * @param divisor
     *            除数
     * @param scale
     *            表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static Double div(Number dividend, Number divisor, Integer scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(dividend.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(divisor.doubleValue()));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param dividend
     *            被除数
     * @param divisor
     *            除数
     * @param scale
     *            表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static Double div(Number dividend, Number divisor, Integer scale, int roundingMode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(dividend.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(divisor.doubleValue()));
        return b1.divide(b2, scale, roundingMode).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param value
     *            需要四舍五入的数字
     * @param scale
     *            小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static Double round(Double value, Integer scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(value));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
