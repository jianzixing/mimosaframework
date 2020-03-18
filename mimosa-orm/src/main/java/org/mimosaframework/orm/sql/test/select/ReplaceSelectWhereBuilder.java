package org.mimosaframework.orm.sql.test.select;

import org.mimosaframework.orm.sql.test.*;

public interface ReplaceSelectWhereBuilder
        extends
        LeftJoinBuilder<TableBuilder<OnBuilder<SelectOnBuilder>>>,
        InnerJoinBuilder<TableBuilder<OnBuilder<SelectOnBuilder>>>,
        WhereBuilder<SelectWhereBuilder> {
}
