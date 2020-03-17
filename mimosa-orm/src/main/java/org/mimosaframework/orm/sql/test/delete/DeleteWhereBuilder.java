package org.mimosaframework.orm.sql.test.delete;

import org.mimosaframework.orm.sql.test.LogicBuilder;
import org.mimosaframework.orm.sql.test.WhereItemBuilder;
import org.mimosaframework.orm.sql.test.WrapperBuilder;

public interface DeleteWhereBuilder
        extends
        WrapperBuilder<LogicBuilder<DeleteWhereBuilder>>,
        WhereItemBuilder<LogicBuilder<DeleteWhereBuilder>> {
}
