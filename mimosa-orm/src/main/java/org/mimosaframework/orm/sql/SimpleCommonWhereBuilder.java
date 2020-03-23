package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.platform.SQLBuilderFactory;
import org.mimosaframework.orm.sql.select.SelectWhereBuilder;

import java.io.Serializable;

public class SimpleCommonWhereBuilder implements CommonWhereBuilder {
    private SQLBuilder sqlBuilder;

    public SimpleCommonWhereBuilder() {
        this.sqlBuilder = SQLBuilderFactory.createSQLBuilder();
    }

    public SQLBuilder getSqlBuilder() {
        return sqlBuilder;
    }

    @Override
    public OperatorFunctionBuilder<LogicBuilder<SelectWhereBuilder>> column(Serializable field) {
        return null;
    }

    @Override
    public OperatorFunctionBuilder<LogicBuilder<SelectWhereBuilder>> column(Class table, Serializable field) {
        return null;
    }

    @Override
    public OperatorFunctionBuilder<LogicBuilder<SelectWhereBuilder>> column(String aliasName, Serializable field) {
        return null;
    }

    @Override
    public LogicBuilder<SelectWhereBuilder> wrapper(AboutChildBuilder builder) {
        return null;
    }
}
