package org.mimosaframework.orm.sql.insert;

public interface InsertValuesBuilder<T> {
    T row(Object... values);
}
