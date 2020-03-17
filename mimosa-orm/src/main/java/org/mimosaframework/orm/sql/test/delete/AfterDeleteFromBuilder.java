package org.mimosaframework.orm.sql.test.delete;

import org.mimosaframework.orm.sql.test.UsingBuilder;
import org.mimosaframework.orm.sql.test.WhereBuilder;

public interface AfterDeleteFromBuilder<T> extends UsingBuilder<WhereBuilder<T>>, WhereBuilder<T> {
}
