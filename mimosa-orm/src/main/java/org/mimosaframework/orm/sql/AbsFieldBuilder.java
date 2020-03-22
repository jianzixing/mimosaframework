package org.mimosaframework.orm.sql;

import java.io.Serializable;

public interface AbsFieldBuilder<T> {
    T all();

    T fields(Class table, Serializable... fields);

    T fields(FieldItem... fieldItems);

    T fields(Fields fieldItems);
}
