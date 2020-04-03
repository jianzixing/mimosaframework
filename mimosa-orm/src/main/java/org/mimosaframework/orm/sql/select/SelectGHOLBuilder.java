package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.*;

public interface SelectGHOLBuilder
        extends
        SelectGroupByBuilder,
        HavingBuilder<HavingWhereBuilder>,
        OrderByBuilder<OrderByNextBuilder>,
        LimitBuilder<UnifyBuilder> {

}
