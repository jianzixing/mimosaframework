package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.AbsTableAliasBuilder;
import org.mimosaframework.orm.sql.UsingBuilder;
import org.mimosaframework.orm.sql.WhereBuilder;

public interface ReplaceDeleteFromBuilder<S, T>
        extends UsingBuilder<AbsTableAliasBuilder<DeleteAsTableBuilder<S>>>, WhereBuilder<T> {
}
