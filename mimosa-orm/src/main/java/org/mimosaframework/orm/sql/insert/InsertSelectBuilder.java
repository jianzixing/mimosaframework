package org.mimosaframework.orm.sql.insert;

import org.mimosaframework.orm.sql.UnifyBuilder;
import org.mimosaframework.orm.sql.ValuesBuilder;

public interface InsertSelectBuilder<T>
        extends
        ValuesBuilder<T> {
    UnifyBuilder select(UnifyBuilder builder);
}
