package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.*;

public interface SelectGHOLBuilder
        extends
        GroupByBuilder<SelectHOLBuilder>,
        HavingBuilder<HavingOperatorFunctionBuilder>,
        OrderByBuilder<OrderByNextBuilder>,
        LimitBuilder<UnifyBuilder> {

}
