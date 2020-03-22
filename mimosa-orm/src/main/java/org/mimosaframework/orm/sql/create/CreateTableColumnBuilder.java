package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.AboutChildBuilder;

public interface CreateTableColumnBuilder<T> {
    T columns(AboutChildBuilder... columns);
}
