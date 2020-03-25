package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.AbsTableAliasBuilder;

public interface SelectTableAliasBuilder<T>
        extends
        AbsTableAliasBuilder<T>,
        ReplaceSelectWhereBuilder {
}
