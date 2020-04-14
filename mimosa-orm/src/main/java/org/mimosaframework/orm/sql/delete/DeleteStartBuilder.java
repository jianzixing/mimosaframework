package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.CommonWhereBuilder;
import org.mimosaframework.orm.sql.FromBuilder;
import org.mimosaframework.orm.sql.WhereBuilder;

public interface DeleteStartBuilder
        extends
        FromBuilder<DeleteTableNameBuilder<WhereBuilder<CommonWhereBuilder<DeleteWhereNextBuilder>>>> {
}
