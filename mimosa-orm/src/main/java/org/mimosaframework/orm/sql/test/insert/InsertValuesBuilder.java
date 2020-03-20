package org.mimosaframework.orm.sql.test.insert;

public interface InsertValuesBuilder<T> {
    T row(Object... values);
}
