package org.mimosaframework.orm.sql.test.update;

import org.mimosaframework.orm.sql.test.SplitBuilder;
import org.mimosaframework.orm.sql.test.WhereBuilder;

public interface ReplaceUpdateValueBuilder
        extends
        SplitBuilder<ReplaceUpdateSetBuilder>,
        WhereBuilder<UpdateWhereBuilder> {
}
