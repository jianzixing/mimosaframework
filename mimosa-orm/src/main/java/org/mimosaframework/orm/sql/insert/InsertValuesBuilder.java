package org.mimosaframework.orm.sql.insert;

import org.mimosaframework.orm.sql.UnifyBuilder;

public interface InsertValuesBuilder<T> extends UnifyBuilder {
    T row(Object... values);
}
