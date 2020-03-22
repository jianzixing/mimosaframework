package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.sql.select.SelectWhereBuilder;

public interface CommonWhereBuilder
        extends
        AboutChildBuilder,
        WrapperBuilder<LogicBuilder<SelectWhereBuilder>>,
        OperatorLinkBuilder<LogicBuilder<SelectWhereBuilder>> {
}
