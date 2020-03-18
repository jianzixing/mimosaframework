package org.mimosaframework.orm.sql.test;

public interface TableBuilder<T> {
    T table(Class table);

    T table(Class... table);

    T table(TableItem tableItem);

    T table(TableItem... tableItem);

    T table(TableItems tableItems);
}
