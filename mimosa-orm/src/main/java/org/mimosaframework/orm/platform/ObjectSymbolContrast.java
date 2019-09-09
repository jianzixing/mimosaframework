package org.mimosaframework.orm.platform;

/**
 * Mysql的类型比较说明
 * https://dev.mysql.com/doc/refman/5.7/en/comparison-operators.html
 */
public interface ObjectSymbolContrast {
    boolean isTrue(Object first, Object second, String symbol);
}
