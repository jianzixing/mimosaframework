package org.mimosaframework.orm.sql;

public interface InsertBuilder<T> extends AboutBuilderAction {
    T insert();
}
