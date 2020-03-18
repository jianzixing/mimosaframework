package org.mimosaframework.orm.sql.test.insert;

public interface InsertValuesBuilder<T> {
    T wrapper(Object... values);
}
