package org.mimosaframework.orm.sql.test.select;

import org.mimosaframework.orm.sql.test.LimitBuilder;
import org.mimosaframework.orm.sql.test.OrderByBuilder;
import org.mimosaframework.orm.sql.test.SortBuilder;

public interface SelectOLBuilder
        extends
        OrderByBuilder<SortBuilder<LimitBuilder>>,
        LimitBuilder {

}
