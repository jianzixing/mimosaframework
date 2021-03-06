package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.CommonWhereBuilder;
import org.mimosaframework.orm.sql.CommonWhereNextBuilder;
import org.mimosaframework.orm.sql.WhereBuilder;

public interface ReplaceUpdateValueBuilder<T extends CommonWhereNextBuilder>
        extends
        ReplaceUpdateSetBuilder,
        WhereBuilder<CommonWhereBuilder<T>> {
}
