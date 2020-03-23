package org.mimosaframework.orm.sql;

public interface UpdateBuilder<T> extends AboutBuilderAction {
    T update();
}
