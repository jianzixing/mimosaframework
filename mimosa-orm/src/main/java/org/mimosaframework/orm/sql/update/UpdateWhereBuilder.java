package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.OperatorLinkBuilder;
import org.mimosaframework.orm.sql.WrapperBuilder;

public interface UpdateWhereBuilder
        extends
        WrapperBuilder<ReplaceUpdateWhereLogicBuilder>,
        OperatorLinkBuilder<ReplaceUpdateWhereLogicBuilder> {
}
