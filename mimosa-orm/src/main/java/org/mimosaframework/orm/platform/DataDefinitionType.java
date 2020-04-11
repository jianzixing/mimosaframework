package org.mimosaframework.orm.platform;

public enum DataDefinitionType {
    CREATE_TABLE,
    DROP_TABLE,

    ADD_COLUMN,
    MODIFY_COLUMN,
    DROP_COLUMN,

    ADD_INDEX,
    MODIFY_INDEX,
    DROP_INDEX,

    ADD_CONS,
    DROP_CONS
}
