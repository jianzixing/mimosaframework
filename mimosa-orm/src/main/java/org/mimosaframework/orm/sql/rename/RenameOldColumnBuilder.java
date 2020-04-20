package org.mimosaframework.orm.sql.rename;

public interface RenameOldColumnBuilder<T> {
    T oldColumn(String field);
}
