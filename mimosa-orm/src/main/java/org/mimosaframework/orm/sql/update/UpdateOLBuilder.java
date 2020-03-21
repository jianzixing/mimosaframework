package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.LimitBuilder;
import org.mimosaframework.orm.sql.OrderByBuilder;
import org.mimosaframework.orm.sql.SortBuilder;
import org.mimosaframework.orm.sql.UnifyBuilder;

public interface UpdateOLBuilder
        extends
        OrderByBuilder<SortBuilder<LimitBuilder<UnifyBuilder>>>,
        LimitBuilder<UnifyBuilder> {
}
