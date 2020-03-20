package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.LimitBuilder;
import org.mimosaframework.orm.sql.OrderByBuilder;
import org.mimosaframework.orm.sql.SortBuilder;

public interface UpdateOLBuilder
        extends
        OrderByBuilder<SortBuilder<LimitBuilder>>,
        LimitBuilder {
}
