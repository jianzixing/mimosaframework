package org.mimosaframework.orm.sql.test;

import java.io.Serializable;

public interface AboutFieldItem<T> extends AboutFieldItemFun<T> {
    T field(Serializable field);

    T field(String tableAliasName, Serializable field);

    T field(Class table, Serializable field);

    T field(Class table, Serializable field, String fieldAliasName);

    T field(String tableAliasName, Serializable field, String fieldAliasName);
}
