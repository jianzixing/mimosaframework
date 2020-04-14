package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.*;

public interface SelectJoinsBuilder
        extends
        LeftJoinBuilder<SelectTableNameBuilder<OnBuilder<CommonWhereBuilder<SelectJoinWhereNextBuilder>>>>,
        InnerJoinBuilder<SelectTableNameBuilder<OnBuilder<CommonWhereBuilder<SelectJoinWhereNextBuilder>>>>,
        WhereBuilder<CommonWhereBuilder<SelectWhereNextBuilder>>,
        SelectGHOLBuilder {
}
