package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.FieldFunBuilder;

import java.io.Serializable;

public interface SelectFieldBuilder<T> extends FieldFunBuilder<T> {
    T all();

    T fields(Serializable... fields);

    T fields(Class table, Serializable... fields);

    T fields(String tableAliasName, Serializable... fields);

    T field(Serializable field, String fieldAliasName);

    T field(Class table, Serializable field, String fieldAliasName);

    T field(String tableAliasName, Serializable field, String fieldAliasName);

    T distinct(Serializable field);

    T distinct(String tableAliasName, Serializable field);

    T distinct(Class table, Serializable field);

    T distinct(Serializable field, String fieldAliasName);

    T distinct(String tableAliasName, Serializable field, String fieldAliasName);

    T distinct(Class table, Serializable field, String fieldAliasName);
}
