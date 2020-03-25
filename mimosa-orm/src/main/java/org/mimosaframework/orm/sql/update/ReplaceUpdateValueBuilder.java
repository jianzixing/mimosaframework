package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.WhereBuilder;

public interface ReplaceUpdateValueBuilder
        extends
        ReplaceUpdateSetBuilder,
        WhereBuilder<UpdateWhereBuilder> {
}
