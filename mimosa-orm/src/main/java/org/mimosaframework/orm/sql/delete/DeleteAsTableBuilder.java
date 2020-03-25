package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.WhereBuilder;

public interface DeleteAsTableBuilder<T>
        extends
        DeleteTableAsBuilder<T>,
        WhereBuilder<T> {
}
