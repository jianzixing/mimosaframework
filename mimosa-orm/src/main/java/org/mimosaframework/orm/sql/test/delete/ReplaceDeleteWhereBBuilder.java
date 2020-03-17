package org.mimosaframework.orm.sql.test.delete;

import org.mimosaframework.orm.sql.test.LimitBuilder;
import org.mimosaframework.orm.sql.test.OrderByBuilder;
import org.mimosaframework.orm.sql.test.WhereBuilder;

public interface ReplaceDeleteWhereBBuilder
        extends
        WhereBuilder<DeleteWhereBuilder>,
        OrderByBuilder<LimitBuilder<Void>> {
}
