package org.mimosaframework.core.utils.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CalcNumber {
    private BigDecimal bg;
    private int scale = 8;

    public CalcNumber(BigDecimal bg) {
        this.bg = bg;
    }

    public static CalcNumber as(int i) {
        return new CalcNumber(new BigDecimal(i));
    }

    public static CalcNumber as(long i) {
        return new CalcNumber(new BigDecimal(i));
    }

    public static CalcNumber as(double i) {
        return new CalcNumber(new BigDecimal(i));
    }

    public static CalcNumber as(String i) {
        return new CalcNumber(new BigDecimal(i));
    }

    public static CalcNumber as(BigDecimal i) {
        return new CalcNumber(i);
    }

    public static CalcNumber as(CalcNumber i) {
        return i;
    }

    private BigDecimal objectToBigDecimal(Object o) {
        if (o.getClass().isAssignableFrom(Integer.class)) {
            return new BigDecimal((int) o);
        }
        if (o.getClass().isAssignableFrom(Long.class)) {
            return new BigDecimal((long) o);
        }
        if (o.getClass().isAssignableFrom(Double.class)) {
            return new BigDecimal(Double.toString((double) o));
        }
        if (o.getClass().isAssignableFrom(Float.class)) {
            return new BigDecimal(Float.toString((float) o));
        }
        if (o.getClass().isAssignableFrom(Short.class)) {
            return new BigDecimal((short) o);
        }
        if (o.getClass().isAssignableFrom(Byte.class)) {
            return new BigDecimal((byte) o);
        }
        if (o.getClass().isAssignableFrom(String.class)) {
            return new BigDecimal((String) o);
        }
        if (o.getClass().isAssignableFrom(BigDecimal.class)) {
            return (BigDecimal) o;
        }
        if (o.getClass().isAssignableFrom(CalcNumber.class)) {
            return ((CalcNumber) o).toBigDecimal();
        }
        throw new IllegalArgumentException("不支持的计算类型");
    }

    public CalcNumber add(Object o) {
        bg = bg.add(objectToBigDecimal(o));
        return this;
    }

    public CalcNumber divide(Object o) {
        BigDecimal to = objectToBigDecimal(o);
        if (to.compareTo(new BigDecimal(0)) == 0) {
            bg = new BigDecimal(0);
        }
        bg = bg.divide(to, scale, BigDecimal.ROUND_HALF_UP);
        return this;
    }

    public CalcNumber divide(Object o, RoundingMode roundingMode) {
        BigDecimal to = objectToBigDecimal(o);
        if (to.compareTo(new BigDecimal(0)) == 0) {
            bg = new BigDecimal(0);
        }
        bg = bg.divide(to, scale, roundingMode);
        return this;
    }

    public CalcNumber divide(Object o, int scale, RoundingMode roundingMode) {
        BigDecimal to = objectToBigDecimal(o);
        if (to.compareTo(new BigDecimal(0)) == 0) {
            bg = new BigDecimal(0);
        }
        bg = bg.divide(to, scale, roundingMode);
        return this;
    }

    public CalcNumber multiply(Object o) {
        bg = bg.multiply(objectToBigDecimal(o));
        return this;
    }

    public CalcNumber subtract(Object o) {
        bg = bg.subtract(objectToBigDecimal(o));
        return this;
    }

    public BigDecimal toBigDecimal() {
        return bg;
    }

    public String toPrice() {
        if (this.bg != null) {
            BigDecimal bigDecimal = this.bg.setScale(2, BigDecimal.ROUND_DOWN);
            DecimalFormat format = new DecimalFormat("#.00");
            // return bigDecimal.toPlainString();
            return format.format(bigDecimal);
        }
        return "0";
    }

    public String format(String format) {
        if (this.bg != null) {
            BigDecimal bigDecimal = this.bg.setScale(2, BigDecimal.ROUND_DOWN);
            DecimalFormat decimalFormat = new DecimalFormat(format);
            // return bigDecimal.toPlainString();
            return decimalFormat.format(bigDecimal);
        }
        return "0";
    }

    public BigDecimal toBigDecimalPrice() {
        if (this.bg != null) {
            return this.bg.setScale(2, BigDecimal.ROUND_DOWN);
        }
        return new BigDecimal(0);
    }

    public String toString(int scale) {
        if (this.bg != null) {
            BigDecimal bigDecimal = this.bg.setScale(scale, BigDecimal.ROUND_DOWN);
            return bigDecimal.toPlainString();
        }
        return "0";
    }

    public String toString(int scale, RoundingMode mode) {
        if (this.bg != null) {
            BigDecimal bigDecimal = this.bg.setScale(scale, mode);
            return bigDecimal.toPlainString();
        }
        return "0";
    }

    public CalcNumber scale(int scale) {
        this.scale = scale;
        return this;
    }

    public int toInteger() {
        return bg.intValue();
    }

    public long toLong() {
        return bg.longValue();
    }

    public double toDouble() {
        return bg.doubleValue();
    }

    public float toFloat() {
        return bg.floatValue();
    }

    public BigInteger toBigInteger() {
        return bg.toBigInteger();
    }

    public String toString() {
        return bg.toPlainString();
    }

    public BigDecimal[] suchAsDividePrice(int size) {
        return this.suchAsDivide(size, 2);
    }

    public BigDecimal[] suchAsDivide(int size, int scale) {
        BigDecimal bigDecimal = this.bg.setScale(scale, BigDecimal.ROUND_DOWN);
        BigDecimal bd = new BigDecimal(size);
        bd.setScale(scale, BigDecimal.ROUND_DOWN);
        BigDecimal dn = bigDecimal.divide(bd, scale, BigDecimal.ROUND_DOWN);

        BigDecimal[] bigDecimals = new BigDecimal[size];
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                bigDecimals[i] = (new BigDecimal(dn.toPlainString())).add(bigDecimal.subtract(dn.multiply(bd)));
            } else {
                bigDecimals[i] = dn;
            }
        }
        return bigDecimals;
    }

    public boolean gte(BigDecimal payPriceIntegralAmount) {
        return this.bg.compareTo(payPriceIntegralAmount) >= 0;
    }

    public boolean lte(BigDecimal payPriceIntegralAmount) {
        return this.bg.compareTo(payPriceIntegralAmount) <= 0;
    }

    public boolean gt(BigDecimal payPriceIntegralAmount) {
        return this.bg.compareTo(payPriceIntegralAmount) > 0;
    }

    public boolean eq(BigDecimal payPriceIntegralAmount) {
        return this.bg.compareTo(payPriceIntegralAmount) == 0;
    }

    public boolean lt(BigDecimal payPriceIntegralAmount) {
        return this.bg.compareTo(payPriceIntegralAmount) < 0;
    }

    public long toPennyPrice() {
        if (this.bg != null) {
            BigDecimal money = this.bg.multiply(new BigDecimal(100));
            return money.longValue();
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(CalcNumber.as(9).divide(3).toPrice());
        System.out.println(CalcNumber.as(10).lte(new BigDecimal(10)));
    }
}
