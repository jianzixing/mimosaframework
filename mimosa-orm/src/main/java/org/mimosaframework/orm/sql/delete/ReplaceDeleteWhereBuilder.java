package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.LimitBuilder;
import org.mimosaframework.orm.sql.OrderByBuilder;
import org.mimosaframework.orm.sql.UnifyBuilder;

public interface ReplaceDeleteWhereBuilder
        extends
        AfterDeleteFromBuilder<DeleteWhereBuilder, DeleteWhereOrderByBuilder>,
        OrderByBuilder<LimitBuilder<UnifyBuilder>> {
}
