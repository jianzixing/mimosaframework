package org.mimosaframework.orm.sql;

public interface UsingBuilder<T> {
    T using(Class... tables);

    T using(TableItem... items);
}
