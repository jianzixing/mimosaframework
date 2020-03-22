package org.mimosaframework.orm.sql;

public interface SelectBuilder<T> extends AboutBuilderAction{
    T select();
}
