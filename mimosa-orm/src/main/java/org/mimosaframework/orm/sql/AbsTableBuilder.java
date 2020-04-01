package org.mimosaframework.orm.sql;

public interface AbsTableBuilder<T> extends UnifyBuilder {
    T table(Class table);
}
