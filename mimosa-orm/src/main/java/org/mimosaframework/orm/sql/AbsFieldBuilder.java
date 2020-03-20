package org.mimosaframework.orm.sql;

public interface AbsFieldBuilder<T> {
    T all();

    T fields(Class table, Object... fields);

    T fields(FieldItem... fieldItems);

    T fields(Fields fieldItems);
}
