package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.LogicBuilder;
import org.mimosaframework.orm.sql.OperatorLinkBuilder;
import org.mimosaframework.orm.sql.WrapperBuilder;

public interface DeleteWhereBuilder
        extends
        WrapperBuilder<LogicBuilder<DeleteWhereBuilder>>,
        OperatorLinkBuilder<LogicBuilder<DeleteWhereBuilder>> {
}
