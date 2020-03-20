package org.mimosaframework.orm.sql;

public interface ValuesBuilder<T> {
    T values(Object... values);
}
