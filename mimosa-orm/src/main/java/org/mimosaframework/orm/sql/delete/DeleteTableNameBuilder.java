package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.AbsTableBuilder;

public interface DeleteTableNameBuilder<T> extends AbsTableBuilder<T> {
    T table(String name);
}
