package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.sql.stamp.KeyColumnType;

/**
 * 再判断配置的数据类型是否和数据库数据类型是否一致
 * 当前系统定义了一套统一的数据类型 {@link KeyColumnType}
 * 不同数据库需要自主转换，这时候需要判断自主转换的数据类型是否和Java数据
 * 类型一致
 */
public enum ColumnCompareType {
    /**
     * 表示当前匹配只匹配数据类型名称即可
     * eg: INT -> int.class
     */
    NONE,

    /**
     * 表示判断当前类型匹配数据类型名称和Java映射类配置的数据长度
     * eg: VARCHAR -> String.class , length -> java mapping length
     */
    JAVA,

    /**
     * 表示判断当前类型匹配数据类型名称和类型定义时的数据长度
     * eg: NUMBER(32) -> long.class , length -> column type length
     */
    SELF
}
