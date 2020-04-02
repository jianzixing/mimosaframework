package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.*;

public interface DeleteWhereNextBuilder
        extends
        CommonWhereNextBuilder<DeleteWhereNextBuilder>,
        OrderByBuilder<OrderByNextCountBuilder>,
        LimitCountBuilder<UnifyBuilder> {

}
