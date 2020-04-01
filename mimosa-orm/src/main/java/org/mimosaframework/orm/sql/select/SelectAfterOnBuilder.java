package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.LogicBuilder;
import org.mimosaframework.orm.sql.WhereBuilder;

public interface SelectAfterOnBuilder
        extends
        WhereBuilder<SelectWhereBuilder>,
        SelectJoinsBuilder,
        LogicBuilder<SelectOnBuilder> {
}
