package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.SortBuilder;
import org.mimosaframework.orm.sql.LimitBuilder;
import org.mimosaframework.orm.sql.OrderByBuilder;

public interface SelectOLBuilder
        extends
        OrderByBuilder<SortBuilder<LimitBuilder>>,
        LimitBuilder {

}
