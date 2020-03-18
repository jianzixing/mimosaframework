package org.mimosaframework.orm.sql.test.update;

import org.mimosaframework.orm.sql.test.LimitBuilder;
import org.mimosaframework.orm.sql.test.OrderByBuilder;
import org.mimosaframework.orm.sql.test.SortBuilder;

public interface UpdateOLBuilder
        extends
        OrderByBuilder<SortBuilder<LimitBuilder>>,
        LimitBuilder {
}
