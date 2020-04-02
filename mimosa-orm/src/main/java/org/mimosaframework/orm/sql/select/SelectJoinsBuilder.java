package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.*;

public interface SelectJoinsBuilder
        extends
        LeftJoinBuilder<AbsTableAliasBuilder<OnBuilder<CommonWhereBuilder<SelectJoinWhereNextBuilder>>>>,
        InnerJoinBuilder<AbsTableAliasBuilder<OnBuilder<CommonWhereBuilder<SelectJoinWhereNextBuilder>>>>,
        WhereBuilder<CommonWhereBuilder<SelectWhereNextBuilder>>,
        SelectGHOLBuilder {
}
