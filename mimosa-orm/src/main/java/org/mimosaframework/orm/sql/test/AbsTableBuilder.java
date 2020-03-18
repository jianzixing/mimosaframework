package org.mimosaframework.orm.sql.test;

public interface AbsTableBuilder<T> {
    T table(Class table);
}
