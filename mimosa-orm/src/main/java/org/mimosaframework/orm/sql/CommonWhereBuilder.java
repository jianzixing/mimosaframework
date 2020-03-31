package org.mimosaframework.orm.sql;

public interface CommonWhereBuilder
        extends
        AboutChildBuilder,
        WrapperBuilder<LogicBuilder<CommonWhereBuilder>>,
        OperatorLinkBuilder<LogicBuilder<CommonWhereBuilder>> {
}
