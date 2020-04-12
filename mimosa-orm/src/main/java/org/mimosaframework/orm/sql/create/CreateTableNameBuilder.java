package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.AbsTableBuilder;

public interface CreateTableNameBuilder<T>
        extends AbsTableBuilder<T> {
    T table(String name);
}
