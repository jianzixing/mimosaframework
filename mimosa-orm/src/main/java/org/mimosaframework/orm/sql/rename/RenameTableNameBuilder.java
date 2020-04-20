package org.mimosaframework.orm.sql.rename;

import org.mimosaframework.orm.sql.AbsTableBuilder;

public interface RenameTableNameBuilder<T>
        extends AbsTableBuilder<T> {
    T table(String name);
}
