package org.mimosaframework.orm.sql.test;

public interface FieldBuilder<T> {
    T all();

    T fields(Class table, Object... fields);

    T fields(FieldItem... fieldItems);

    T fields(FieldItems fieldItems);
}
