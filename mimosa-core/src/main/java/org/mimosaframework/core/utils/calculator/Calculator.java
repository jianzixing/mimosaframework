package org.mimosaframework.core.utils.calculator;

import java.math.BigDecimal;

/**
 * 计算器
 */
public final class Calculator {
    public static CalcNumber add(BigDecimal b1, BigDecimal b2) {
        return new CalcNumber(b1.add(b2));
    }

    public static CalcNumber subtract(BigDecimal b1, BigDecimal b2) {
        return new CalcNumber(b1.subtract(b2));
    }

    public static CalcNumber multiply(BigDecimal b1, BigDecimal b2) {
        return new CalcNumber(b1.multiply(b2));
    }

    public static CalcNumber divide(BigDecimal b1, BigDecimal b2) {
        return new CalcNumber(b1.divide(b2));
    }

    public static CalcNumber add(BigDecimal... bd) {
        BigDecimal r = null;
        for (BigDecimal b : bd) {
            if (r == null) {
                r = b;
            } else {
                r.add(b);
            }
        }
        return new CalcNumber(r);
    }

    public static CalcNumber subtract(BigDecimal... bd) {
        BigDecimal r = null;
        for (BigDecimal b : bd) {
            if (r == null) {
                r = b;
            } else {
                r.subtract(b);
            }
        }
        return new CalcNumber(r);
    }

    public static CalcNumber multiply(BigDecimal... bd) {
        BigDecimal r = null;
        for (BigDecimal b : bd) {
            if (r == null) {
                r = b;
            } else {
                r.multiply(b);
            }
        }
        return new CalcNumber(r);
    }

    public static CalcNumber divide(BigDecimal... bd) {
        BigDecimal r = null;
        for (BigDecimal b : bd) {
            if (r == null) {
                r = b;
            } else {
                r.divide(b);
            }
        }
        return new CalcNumber(r);
    }
}
