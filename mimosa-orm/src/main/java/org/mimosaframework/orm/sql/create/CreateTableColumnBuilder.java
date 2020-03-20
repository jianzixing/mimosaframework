package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.AboutTableColumn;

public interface CreateTableColumnBuilder<T> {
    T columns(AboutTableColumn... columns);
}
