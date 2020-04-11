package org.mimosaframework.orm.sql.drop;

import org.mimosaframework.orm.sql.AbsTableBuilder;

public interface DropTableNameBuilder<T>
        extends AbsTableBuilder<T> {
    T table(String name);
}
