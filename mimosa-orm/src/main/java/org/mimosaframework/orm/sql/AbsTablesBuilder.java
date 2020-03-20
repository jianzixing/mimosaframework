package org.mimosaframework.orm.sql;

public interface AbsTablesBuilder<T> extends AbsTableBuilder<T> {
    T table(Class... table);

    T table(TableItem tableItem);

    T table(TableItem... tableItem);

    T table(TableItems tableItems);
}
