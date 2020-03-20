package org.mimosaframework.orm.sql.test;

import java.io.Serializable;

public interface AbsWhereColumnBuilder<T> {
    T column(Serializable field);

    T column(Class table, Serializable field);

    T column(String aliasName, Serializable field);

    T column(Class tableA, Serializable fieldA, Class tableB, Serializable fieldB);

    T column(String aliasNameA, Serializable fieldA, String aliasNameB, Serializable fieldB);
}
