package org.mimosaframework.orm.sql.test.create;

public interface CreateTableColumnBuilder<T> {
    T columns(TableColumnAbout... columns);
}
