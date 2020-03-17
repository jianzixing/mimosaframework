package org.mimosaframework.orm.sql.test.select;

import org.mimosaframework.orm.sql.test.LogicBuilder;
import org.mimosaframework.orm.sql.test.WhereBuilder;

public interface SelectAfterOnBuilder
        extends
        WhereBuilder<SelectWhereBuilder>,
        ReplaceSelectWhereBuilder,
        LogicBuilder<SelectOnBuilder> {
}
