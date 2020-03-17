package org.mimosaframework.orm.sql.test;

public interface TableBuilder<T> {
    T table(Class table);

    T tables(Class... table);
}
