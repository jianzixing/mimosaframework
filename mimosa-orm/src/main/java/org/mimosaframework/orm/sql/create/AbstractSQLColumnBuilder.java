package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.sql.AbsColumnBuilder;
import org.mimosaframework.orm.sql.AbstractSQLBuilder;
import org.mimosaframework.orm.sql.KeyBuilder;
import org.mimosaframework.orm.sql.NullBuilder;

import java.io.Serializable;

public abstract class AbstractSQLColumnBuilder
        extends
        AbstractSQLBuilder
        implements
        AbsColumnBuilder,
        ColumnTypeBuilder,
        CreateColumnAssistBuilder {

    @Override
    public SQLBuilder getSqlBuilder() {
        return this.sqlBuilder;
    }

    @Override
    public Object column(Serializable field) {
        this.sqlBuilder.addWrapString(field.toString());
        return this;
    }

    @Override
    public CreateColumnAssistBuilder autoIncrement() {
        this.sqlBuilder.AUTO_INCREMENT();
        return this;
    }

    @Override
    public CreateColumnAssistBuilder collate(String collate) {
        this.sqlBuilder.COLLATE().addString(collate);
        return this;
    }

    @Override
    public CreateColumnAssistBuilder comment(String comment) {
        this.sqlBuilder.COMMENT().addQuotesString(comment);
        return this;
    }

    @Override
    public CreateColumnAssistBuilder defaults() {
        this.sqlBuilder.DEFAULT();
        return this;
    }

    @Override
    public CreateColumnAssistBuilder key() {
        this.sqlBuilder.KEY();
        return this;
    }

    @Override
    public NullBuilder<CreateColumnAssistBuilder> not() {
        this.sqlBuilder.NOT();
        return this;
    }

    @Override
    public CreateColumnAssistBuilder nullable() {
        this.sqlBuilder.NULL();
        return this;
    }

    @Override
    public KeyBuilder<CreateColumnAssistBuilder> primary() {
        this.sqlBuilder.PRIMARY();
        return this;
    }

    @Override
    public CreateColumnAssistBuilder unique() {
        this.sqlBuilder.UNIQUE();
        return this;
    }

    @Override
    public Object intType() {
        this.sqlBuilder.addString("INT");
        return this;
    }

    @Override
    public Object varchar(int len) {
        this.sqlBuilder.addString("VARCHAR(" + len + ")");
        return this;
    }

    @Override
    public Object charType(int len) {
        this.sqlBuilder.addString("CHAR(" + len + ")");
        return this;
    }

    @Override
    public Object blob() {
        this.sqlBuilder.addString("BLOB");
        return this;
    }

    @Override
    public Object text() {
        this.sqlBuilder.addString("TEXT");
        return this;
    }

    @Override
    public Object tinyint() {
        this.sqlBuilder.addString("TINYINT");
        return this;
    }

    @Override
    public Object smallint() {
        this.sqlBuilder.addString("SMALLINT");
        return this;
    }

    @Override
    public Object mediumint() {
        this.sqlBuilder.addString("MEDIUMINT");
        return this;
    }

    @Override
    public Object bit() {
        this.sqlBuilder.addString("BIT");
        return this;
    }

    @Override
    public Object bigint() {
        this.sqlBuilder.addString("BIGINT");
        return this;
    }

    @Override
    public Object floatType() {
        this.sqlBuilder.addString("FLOAT");
        return this;
    }

    @Override
    public Object doubleType() {
        this.sqlBuilder.addString("DOUBLE");
        return this;
    }

    @Override
    public Object decimal(int len, int scale) {
        this.sqlBuilder.addString("DECIMAL(" + len + "," + scale + ")");
        return this;
    }

    @Override
    public Object booleanType() {
        this.sqlBuilder.addString("BOOLEAN");
        return this;
    }

    @Override
    public Object date() {
        this.sqlBuilder.addString("DATE");
        return this;
    }

    @Override
    public Object time() {
        this.sqlBuilder.addString("TIME");
        return this;
    }

    @Override
    public Object datetime() {
        this.sqlBuilder.addString("DATETIME");
        return this;
    }

    @Override
    public Object timestamp() {
        this.sqlBuilder.addString("TIMESTAMP");
        return this;
    }

    @Override
    public Object year() {
        this.sqlBuilder.addString("YEAR");
        return this;
    }
}
