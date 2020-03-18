package org.mimosaframework.orm.sql.test.select;

import org.mimosaframework.orm.sql.test.*;

public interface ReplaceSelectWhereBuilder
        extends
        LeftJoinBuilder<AbsTablesBuilder<OnBuilder<SelectOnBuilder>>>,
        InnerJoinBuilder<AbsTablesBuilder<OnBuilder<SelectOnBuilder>>>,
        WhereBuilder<SelectWhereBuilder> {
}
