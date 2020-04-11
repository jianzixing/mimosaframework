package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.AbsTableBuilder;

public interface AlterTableNameBuilder<T>
        extends AbsTableBuilder<T> {
    T table(String name);
}
