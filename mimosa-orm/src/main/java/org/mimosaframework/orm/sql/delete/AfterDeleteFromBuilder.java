package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.UsingBuilder;
import org.mimosaframework.orm.sql.WhereBuilder;

public interface AfterDeleteFromBuilder<S, T> extends UsingBuilder<WhereBuilder<S>>, WhereBuilder<T> {
}
