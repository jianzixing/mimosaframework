package org.mimosaframework.orm.sql.test.select;

import org.mimosaframework.orm.sql.test.LogicBuilder;
import org.mimosaframework.orm.sql.test.WhereItemBuilder;
import org.mimosaframework.orm.sql.test.WrapperBuilder;

public interface SelectWhereBuilder
        extends
        WrapperBuilder<LogicBuilder<SelectWhereBuilder>>,
        WhereItemBuilder<LogicBuilder<SelectWhereBuilder>> {
}
