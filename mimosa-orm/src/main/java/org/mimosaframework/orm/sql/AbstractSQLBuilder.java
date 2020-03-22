package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.platform.SQLBuilderCombine;

public abstract class AbstractSQLBuilder
        implements
        SQLMappingChannel {

    protected SQLBuilder sqlBuilder;
    protected byte body = 0;
    protected String lastPlaceholderName;

    public AbstractSQLBuilder() {
        this.sqlBuilder = this.createSQLBuilder();
    }

    protected abstract SQLBuilder createSQLBuilder();

    public SQLBuilderCombine getPlanSql() {
        return this.sqlBuilder.toSQLString();
    }
}
