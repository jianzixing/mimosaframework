package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.*;

public interface SelectOLBuilder
        extends
        OrderByBuilder<OrderByNextBuilder>,
        LimitBuilder<UnifyBuilder> {

}
