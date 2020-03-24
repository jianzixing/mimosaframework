package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.FieldFunBuilder;

import java.io.Serializable;

public interface SelectFieldBuilder<T> extends FieldFunBuilder<T> {
    T all();

    T field(Class table, Serializable... fields);

    T field(String tableAliasName, Serializable... fields);

    T field(Class table, Serializable field, String fieldAliasName);

    T field(String tableAliasName, Serializable field, String fieldAliasName);
}
