package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.*;

public interface ReplaceSelectWhereBuilder
        extends
        LeftJoinBuilder<AbsTablesBuilder<OnBuilder<SelectOnBuilder>>>,
        InnerJoinBuilder<AbsTablesBuilder<OnBuilder<SelectOnBuilder>>>,
        WhereBuilder<SelectWhereBuilder> {
}
