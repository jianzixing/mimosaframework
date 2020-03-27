package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.platform.SQLMappingField;
import org.mimosaframework.orm.platform.SQLMappingTable;
import org.mimosaframework.orm.sql.*;

import java.io.Serializable;

public abstract class AbstractSQLAlterBuilder
        extends
        AbstractSQLBuilder
        implements

        RedefineAlterBuilder {

    protected boolean isAutoNumber = false;
    protected int isColumnNeedWrap = 0;

    @Override
    public Object alter() {
        this.sqlBuilder.ALTER();
        return this;
    }

    @Override
    public Object name(Serializable value) {
        this.sqlBuilder.addString(value.toString());
        if (this.isColumnNeedWrap == 2) this.isColumnNeedWrap = 3;
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
    public Object database() {
        this.sqlBuilder.DATABASE();
        this.body = 1;
        return this;
    }

    @Override
    public Object column(Serializable field) {
        if (this.isColumnNeedWrap == 2 || this.isColumnNeedWrap == 3) {
            this.sqlBuilder.addParenthesisStart();
        }
        if (this.isMappingTable) {
            this.sqlBuilder.addMappingField(new SQLMappingField(field));
        } else {
            this.sqlBuilder.addWrapString(field.toString());
        }
        if (this.isColumnNeedWrap == 2 || this.isColumnNeedWrap == 3) {
            this.sqlBuilder.addParenthesisEnd();
        }
        return this;
    }

    @Override
    public Object columns(Serializable... fields) {
        int i = 0;
        this.sqlBuilder.addParenthesisStart();
        for (Serializable field : fields) {
            this.sqlBuilder.addMappingField(new SQLMappingField(field));
            i++;
            if (i != fields.length) this.sqlBuilder.addSplit();
        }
        this.sqlBuilder.addParenthesisEnd();
        return this;
    }

    @Override
    public Object table(Class table) {
        this.sqlBuilder.TABLE();
        this.sqlBuilder.addMappingTable(new SQLMappingTable(table));
        this.isMappingTable = true;
        this.body = 1;
        this.isAutoNumber = true;
        return this;
    }

    @Override
    public Object after() {
        this.sqlBuilder.AFTER();
        return this;
    }

    @Override
    public Object autoIncrement() {
        this.sqlBuilder.AUTO_INCREMENT();
        return this;
    }

    @Override
    public Object column() {
        this.sqlBuilder.COLUMN();
        this.body = 2;
        return this;
    }

    @Override
    public Object add() {
        this.sqlBuilder.ADD();
        this.isAutoNumber = false;
        this.isColumnNeedWrap = 0;
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

    @Override
    public Object comment(String comment) {
        this.sqlBuilder.COMMENT().addQuotesString(comment);
        return this;
    }

    @Override
    public Object index() {
        this.sqlBuilder.INDEX();
        this.body = 2;
        this.isColumnNeedWrap = 2;
        return this;
    }

    @Override
    public Object split() {
        this.sqlBuilder.addSplit();
        this.body = 1;
        return this;
    }

    @Override
    public Object drop() {
        this.sqlBuilder.DROP();
        this.body = 2;
        this.isAutoNumber = false;
        this.isColumnNeedWrap = 0;
        return this;
    }

    @Override
    public Object key() {
        this.sqlBuilder.KEY();
        if (this.isColumnNeedWrap == 1) this.isColumnNeedWrap = 2;
        return this;
    }

    @Override
    public Object primary() {
        this.sqlBuilder.PRIMARY();
        this.isColumnNeedWrap = 1;
        return this;
    }

    @Override
    public Object to() {
        this.sqlBuilder.TO();
        return this;
    }

    @Override
    public Object change() {
        this.sqlBuilder.CHANGE();
        this.body = 2;
        this.isAutoNumber = false;
        this.isColumnNeedWrap = 0;
        return this;
    }

    @Override
    public Object modify() {
        this.sqlBuilder.MODIFY();
        this.body = 2;
        this.isAutoNumber = false;
        this.isColumnNeedWrap = 0;
        return this;
    }

    @Override
    public Object newColumn(Serializable field) {
        this.sqlBuilder.addMappingField(new SQLMappingField(field));
        return this;
    }

    @Override
    public Object oldColumn(Serializable field) {
        this.sqlBuilder.addMappingField(new SQLMappingField(field));
        return this;
    }

    @Override
    public Object rename() {
        this.sqlBuilder.RENAME();
        this.isAutoNumber = false;
        this.isColumnNeedWrap = 0;
        return this;
    }

    @Override
    public Object fullText() {
        this.sqlBuilder.FULLTEXT();
        return this;
    }

    @Override
    public Object unique() {
        this.sqlBuilder.UNIQUE();
        this.isColumnNeedWrap = 2;
        return this;
    }

    @Override
    public Object value(int number) {
        if (this.isAutoNumber) {
            this.sqlBuilder.addEqualMark();
            this.isAutoNumber = false;
        }
        this.sqlBuilder.addString(number + "");
        return this;
    }
}
