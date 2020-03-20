package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.OperatorLinkBuilder;
import org.mimosaframework.orm.sql.WrapperBuilder;

public interface SelectWhereBuilder
        extends
        WrapperBuilder<ReplaceSelectLogicBuilder>,
        OperatorLinkBuilder<ReplaceSelectLogicBuilder> {
}
