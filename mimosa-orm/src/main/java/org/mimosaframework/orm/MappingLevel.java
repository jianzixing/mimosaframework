package org.mimosaframework.orm;

/**
 * @author yangankang
 */
public enum MappingLevel {
    /**
     * 什么都不做
     */
    NOTHING,

    /**
     * 将不匹配的字段打印到控制台
     */
    WARN,

    /**
     * 可建立表或者字段，一旦建立则不会修改删除，只能手动删除
     * 每一次启动时检查一遍如果表或者字段没有建立则建立表或者字段
     */
    CREATE,

    /**
     * 可建立表或者字段，也可以修改表或者字段的任何信息
     * 每一次系统启动会检查一遍如果配置不同则修改数据库和映射的数据结构一致
     */
    UPDATE,

    /**
     * 可删除字段,但不会删除表
     */
    DROP
}
