package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.*;

public interface SelectGHOLBuilder
        extends
        SelectGroupByBuilder,
        HavingBuilder<HavingOperatorFunctionBuilder>,
        OrderByBuilder<OrderByNextBuilder>,
        LimitBuilder<UnifyBuilder> {

}
