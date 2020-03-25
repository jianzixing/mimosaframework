package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.*;

public interface ReplaceSelectWhereBuilder
        extends
        LeftJoinBuilder<AbsTableAliasBuilder<OnBuilder<SelectOnBuilder>>>,
        InnerJoinBuilder<AbsTableAliasBuilder<OnBuilder<SelectOnBuilder>>>,
        WhereBuilder<SelectWhereBuilder> {
}
