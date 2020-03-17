package org.mimosaframework.orm.sql.test;

public interface TableRenameBuilder<T> {
    T table(TableItem tableItem);

    T table(TableItem... tableItem);
}
