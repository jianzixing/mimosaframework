package org.mimosaframework.orm.sql.test.create;

import org.mimosaframework.orm.sql.test.AboutTableColumn;

public interface CreateTableColumnBuilder<T> {
    T columns(AboutTableColumn... columns);
}
