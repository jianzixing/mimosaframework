package org.mimosaframework.orm.sql.test;

public interface UsingBuilder<T> {
    T using(Class... tables);

    T using(TableItem... items);
}
