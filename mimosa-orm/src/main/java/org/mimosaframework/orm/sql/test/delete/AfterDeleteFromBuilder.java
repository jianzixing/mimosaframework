package org.mimosaframework.orm.sql.test.delete;

import org.mimosaframework.orm.sql.test.UsingBuilder;
import org.mimosaframework.orm.sql.test.WhereBuilder;

public interface AfterDeleteFromBuilder<S, T> extends UsingBuilder<WhereBuilder<S>>, WhereBuilder<T> {
}
