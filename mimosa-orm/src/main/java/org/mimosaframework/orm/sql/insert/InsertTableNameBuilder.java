package org.mimosaframework.orm.sql.insert;

import org.mimosaframework.orm.sql.AbsTableBuilder;

public interface InsertTableNameBuilder<T> extends AbsTableBuilder<T> {
    T table(String name);
}
