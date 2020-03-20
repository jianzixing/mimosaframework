package org.mimosaframework.orm.sql;

public interface INEBuilder<T> {
    T ifNotExist();
}
