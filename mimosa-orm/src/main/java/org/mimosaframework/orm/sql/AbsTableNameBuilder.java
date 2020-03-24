package org.mimosaframework.orm.sql;

public interface AbsTableNameBuilder<T> extends AbsNameBuilder<T> {
    T name(Class table);
}
