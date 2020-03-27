package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.platform.SQLMappingField;
import org.mimosaframework.orm.platform.SQLMappingTable;
import org.mimosaframework.orm.sql.*;

import java.io.Serializable;

public abstract class AbstractSQLCreateBuilder
        extends
        AbstractSQLBuilder

        implements
        RedefineCreateBuilder {

    @Override
    public Object create() {
        this.sqlBuilder.CREATE();
        return this;
    }

    @Override
    public Object database() {
        this.sqlBuilder.DATABASE();
        this.type = 1;
        return this;
    }

    @Override
    public Object name(Serializable value) {
        this.sqlBuilder.addWrapString(value.toString());
        if (this.type == 2 && this.body == 1) {
            this.sqlBuilder.addParenthesisStart();
        }
        return this;
    }

    @Override
    public Object name(Class table) {
        this.sqlBuilder.addMappingTable(new SQLMappingTable(table));
        if (this.type == 2 && this.body == 1) {
            this.sqlBuilder.addParenthesisStart();
        }
        return this;
    }

    @Override
    public Object charset(String charset) {
        this.sqlBuilder.CHARSET().addString(charset);
        return this;
    }

    @Override
    public Object collate(String collate) {
        this.sqlBuilder.COLLATE().addString(collate);
        return this;
    }

    @Override
    public Object ifNotExist() {
        this.sqlBuilder.IF().NOT().EXISTS();
        return this;
    }

    @Override
    public Object extra(String sql) {
        this.sqlBuilder.addString(sql);
        return this;
    }

    @Override
    public Object table() {
        this.sqlBuilder.TABLE();
        this.type = 2;
        this.body = 1;
        return this;
    }

    @Override
    public Object table(Class table) {
        this.sqlBuilder.addMappingTable(new SQLMappingTable(table));
        return this;
    }

    @Override
    public Object fullText() {
        this.sqlBuilder.FULLTEXT();
        return this;
    }

    @Override
    public Object index() {
        this.sqlBuilder.INDEX();
        this.type = 3;
        return this;
    }

    @Override
    public Object on() {
        this.sqlBuilder.ON();
        return this;
    }

    @Override
    public Object unique() {
        this.sqlBuilder.UNIQUE();
        this.type = 4;
        return this;
    }

    @Override
    public Object columns(Serializable... columns) {
        if (columns != null) {
            int i = 0;
            for (Serializable column : columns) {
                this.sqlBuilder.addWrapString(column.toString());
                i++;
                if (i != columns.length) this.sqlBuilder.addSplit();
            }
        }
        return this;
    }


    @Override
    public Object column(Serializable field) {
        this.sqlBuilder.addMappingField(new SQLMappingField(field));

        return this;
    }

    @Override
    public Object autoIncrement() {
        this.sqlBuilder.AUTO_INCREMENT();
        return this;
    }

    @Override
    public Object comment(String comment) {
        this.sqlBuilder.COMMENT().addQuotesString(comment);
        return this;
    }

    @Override
    public Object defaults() {
        this.sqlBuilder.DEFAULT();
        return this;
    }

    @Override
    public Object key() {
        this.sqlBuilder.KEY();
        return this;
    }

    @Override
    public Object not() {
        this.sqlBuilder.NOT();
        return this;
    }

    @Override
    public Object nullable() {
        this.sqlBuilder.NULL();
        return this;
    }

    @Override
    public Object primary() {
        this.sqlBuilder.PRIMARY();
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
