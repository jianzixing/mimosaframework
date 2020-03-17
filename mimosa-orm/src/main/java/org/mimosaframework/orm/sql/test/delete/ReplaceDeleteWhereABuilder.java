package org.mimosaframework.orm.sql.test.delete;

import org.mimosaframework.orm.sql.test.LimitBuilder;
import org.mimosaframework.orm.sql.test.OrderByBuilder;

public interface ReplaceDeleteWhereABuilder
        extends
        AfterDeleteFromBuilder<DeleteWhereBuilder>,
        OrderByBuilder<LimitBuilder<Void>> {
}
