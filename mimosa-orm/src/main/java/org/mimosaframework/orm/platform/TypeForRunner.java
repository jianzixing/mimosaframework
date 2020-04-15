package org.mimosaframework.orm.platform;

/**
 * 数据库SQL操作类型
 * 每一种操作类型对应的返回值及处理方式不一样
 */
public enum TypeForRunner {
    ALTER,
    CREATE,
    DELETE,
    DROP,
    INSERT,
    SELECT,
    UPDATE,

    OTHER
}
