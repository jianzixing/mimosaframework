package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.AbsTableAliasBuilder;
import org.mimosaframework.orm.sql.WhereBuilder;

public interface DeleteTableAsBuilder<T>
        extends
        AbsTableAliasBuilder<DeleteAsTableBuilder<T>>,
        WhereBuilder<T> {
}
