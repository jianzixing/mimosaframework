package org.mimosaframework.orm.sql.test.delete;

public interface BeforeDeleteFromBuilder<T> {
    T table(Class table);

    T tables(Class... table);

    T tables(String... aliasNames);
}
