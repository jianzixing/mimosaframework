package org.mimosaframework.orm.platform;

/**
 * 数据库SQL操作类型
 * 每一种操作类型对应的返回值及处理方式不一样
 */
public enum ChangerClassify {
    ALTER,
    CREATE,
    DELETE,
    DROP,
    INSERT,
    SELECT,
    UPDATE,

    CREATE_TABLE,
    CREATE_INDEX,
    UPDATE_FIELD,
    UPDATE_PRIMARY_KEY,
    CREATE_FIELD,
    DELETE_OBJECT,
    ADD_OBJECT,
    ADD_OBJECTS,
    SELECT_PRIMARY_KEY,
    COUNT,
    UPDATE_OBJECT,
    DROP_INDEX,
    DROP_TABLE,
    DROP_FIELD,
    UPDATE_FIELD_AUTO_INCREMENT,
    SILENT
}
