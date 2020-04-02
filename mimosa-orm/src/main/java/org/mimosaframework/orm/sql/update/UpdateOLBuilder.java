package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.*;

public interface UpdateOLBuilder
        extends
        OrderByBuilder<OrderByNextCountBuilder>,
        LimitCountBuilder<UnifyBuilder> {
}
