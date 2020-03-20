package org.mimosaframework.orm.sql;

public interface AbsTableBuilder<T> {
    T table(Class table);
}
