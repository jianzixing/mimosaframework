package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.AbsTableBuilder;
import org.mimosaframework.orm.sql.WhereBuilder;

public interface DeleteTableAsBuilder<T>
        extends
        AbsTableBuilder<DeleteAsTableBuilder<T>>,
        WhereBuilder<T> {
}
