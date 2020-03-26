package org.mimosaframework.orm.sql;

public abstract class AbstractValueSQLBuilder
        extends AbstractSQLBuilder
        implements AbsValueBuilder {

    @Override
    public Object value(Object value) {
        this.sqlBuilder.addDataPlaceholder(this.lastPlaceholderName, value);
        return this;
    }
}
