package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.*;

public interface SelectHOLBuilder
        extends
        HavingBuilder<HavingOperatorFunctionBuilder>,
        OrderByBuilder<OrderByNextBuilder>,
        LimitBuilder<UnifyBuilder> {

}
