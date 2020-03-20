package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.sql.select.SelectWhereBuilder;

public interface CommonWhereBuilder
        extends
        WrapperBuilder<LogicBuilder<SelectWhereBuilder>>,
        OperatorLinkBuilder<LogicBuilder<SelectWhereBuilder>> {
}
