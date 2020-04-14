package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.LimitBuilder;
import org.mimosaframework.orm.sql.OrderByBuilder;
import org.mimosaframework.orm.sql.OrderByNextBuilder;
import org.mimosaframework.orm.sql.UnifyBuilder;

public interface SelectOLBuilder
        extends
        OrderByBuilder<OrderByNextBuilder>,
        LimitBuilder<UnifyBuilder> {

}
