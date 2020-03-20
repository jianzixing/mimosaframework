package org.mimosaframework.orm.sql;

import java.io.Serializable;

public interface AbsWhereColumnBuilder<T> {
    T column(Serializable field);

    T column(Class table, Serializable field);

    T column(String aliasName, Serializable field);
}
