package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.*;

public interface DeleteStartBuilder
        extends
        FromBuilder<AbsTableBuilder<WhereBuilder<CommonWhereBuilder<DeleteWhereNextBuilder>>>> {
}
