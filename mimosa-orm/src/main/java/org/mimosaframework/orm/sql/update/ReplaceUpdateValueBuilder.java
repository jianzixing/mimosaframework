package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.SplitBuilder;
import org.mimosaframework.orm.sql.WhereBuilder;

public interface ReplaceUpdateValueBuilder
        extends
        SplitBuilder<ReplaceUpdateSetBuilder>,
        WhereBuilder<UpdateWhereBuilder> {
}
