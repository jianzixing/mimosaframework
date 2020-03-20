package org.mimosaframework.orm.sql.test;

import org.mimosaframework.orm.sql.test.select.SelectWhereBuilder;

public interface CommonWhereBuilder
        extends
        WrapperBuilder<LogicBuilder<SelectWhereBuilder>>,
        OperatorLinkBuilder<LogicBuilder<SelectWhereBuilder>> {
}
