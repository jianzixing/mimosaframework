package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.AsBuilder;
import org.mimosaframework.orm.sql.WhereBuilder;

public interface DeleteAsTableBuilder<T>
        extends
        AsBuilder<DeleteTableAsBuilder<T>>,
        WhereBuilder<T> {
}
