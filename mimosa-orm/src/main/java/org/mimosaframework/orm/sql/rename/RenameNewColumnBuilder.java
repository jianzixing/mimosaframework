package org.mimosaframework.orm.sql.rename;

public interface RenameNewColumnBuilder<T> {
    T newColumn(String field);
}
