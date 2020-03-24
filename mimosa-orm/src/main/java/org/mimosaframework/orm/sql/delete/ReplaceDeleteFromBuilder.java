package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.AbsTableBuilder;
import org.mimosaframework.orm.sql.UsingBuilder;
import org.mimosaframework.orm.sql.WhereBuilder;

public interface ReplaceDeleteFromBuilder<S, T>
        extends UsingBuilder<AbsTableBuilder<DeleteAsTableBuilder<S>>>, WhereBuilder<T> {
}
