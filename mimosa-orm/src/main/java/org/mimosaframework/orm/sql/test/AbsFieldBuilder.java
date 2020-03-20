package org.mimosaframework.orm.sql.test;

public interface AbsFieldBuilder<T> {
    T all();

    T fields(Class table, Object... fields);

    T fields(FieldItem... fieldItems);

    T fields(FieldItems fieldItems);

    T fun(FieldFunBuilder funBuilder);

    T fun(FieldFunBuilder... funBuilder);

    T fun(FieldFunItems fieldFunItems);
}
