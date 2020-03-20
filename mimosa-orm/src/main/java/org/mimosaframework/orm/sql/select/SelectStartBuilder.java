package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.AbsFieldBuilder;
import org.mimosaframework.orm.sql.AbsTablesBuilder;
import org.mimosaframework.orm.sql.FromBuilder;

public interface SelectStartBuilder
        extends
        AbsFieldBuilder<FromBuilder<AbsTablesBuilder<ReplaceSelectWhereBuilder>>> {
}
