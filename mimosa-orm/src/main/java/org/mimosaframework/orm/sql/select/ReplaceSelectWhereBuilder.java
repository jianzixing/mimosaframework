package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.*;

public interface ReplaceSelectWhereBuilder
        extends
        LeftJoinBuilder<AbsTableBuilder<SelectJoinAsTableBuilder<SelectOnBuilder>>>,
        InnerJoinBuilder<AbsTableBuilder<SelectJoinAsTableBuilder<SelectOnBuilder>>>,
        WhereBuilder<SelectWhereBuilder> {
}
