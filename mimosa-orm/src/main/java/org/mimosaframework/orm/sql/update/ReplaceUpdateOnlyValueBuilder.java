package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.CommonWhereBuilder;
import org.mimosaframework.orm.sql.CommonWhereNextBuilder;
import org.mimosaframework.orm.sql.WhereBuilder;

public interface ReplaceUpdateOnlyValueBuilder<T extends CommonWhereNextBuilder>
        extends
        ReplaceUpdateOnlySetBuilder,
        WhereBuilder<CommonWhereBuilder<T>> {
}
