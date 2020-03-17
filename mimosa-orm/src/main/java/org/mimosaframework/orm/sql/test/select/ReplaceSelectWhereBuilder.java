package org.mimosaframework.orm.sql.test.select;

import org.mimosaframework.orm.sql.test.*;

public interface ReplaceSelectWhereBuilder
        extends
        LeftJoinBuilder<TablesBuilder<OnBuilder<SelectOnBuilder>>>,
        InnerJoinBuilder<TablesBuilder<OnBuilder<SelectOnBuilder>>>,
        WhereBuilder<SelectWhereBuilder> {
}
