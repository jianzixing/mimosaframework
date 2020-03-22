package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.create.ColumnTypeBuilder;

import java.io.Serializable;

public abstract class AbstractSQLAlterBuilder
        extends
        AbstractSQLBuilder
        implements
        SQLMappingChannel,

        AlterBuilder,
        DatabaseBuilder,
        AbsNameBuilder,
        CharsetBuilder,
        CollateBuilder,
        AbsTableBuilder,
        AlterAddBuilder,
        ColumnBuilder,
        ColumnTypeBuilder,
        AutoIncrementBuilder,
        AfterBuilder,
        AbsColumnBuilder,
        IndexBuilder,
        CommentBuilder,
        SplitBuilder,
        AlterDropBuilder,
        AlterModifyBuilder,
        AlterChangeBuilder,
        AlterOldColumnBuilder,
        AlterNewColumnBuilder,
        PrimaryBuilder,
        KeyBuilder,
        AlterRenameBuilder,
        ToBuilder {

    @Override
    public Object alter() {
        this.sqlBuilder.ALTER();
        return this;
    }

    @Override
    public Object name(Serializable value) {
        this.sqlBuilder.addString(value.toString());
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
        this.sqlBuilder.addWrapString(field.toString());
        return this;
    }

    @Override
    public Object table(Class table) {
        MappingTable mappingTable = this.getMappingTableByClass(table);
        this.sqlBuilder.addString(mappingTable.getMappingTableName());
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
        return this;
    }

    @Override
    public Object add() {
        this.sqlBuilder.ADD();
        return this;
    }

    @Override
    public Object intType() {
        this.sqlBuilder.addString("INT");
        return this;
    }

    @Override
    public Object varchar(int len) {
        this.sqlBuilder.addString("VARCHAR").addString("" + len);
        return this;
    }

    @Override
    public Object charType(int len) {
        this.sqlBuilder.addString("CHAR").addString("" + len);
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
        this.sqlBuilder.addWrapString(comment);
        return this;
    }

    @Override
    public Object index() {
        this.sqlBuilder.INDEX();
        return this;
    }

    @Override
    public Object split() {
        this.sqlBuilder.addSplit();
        return this;
    }

    @Override
    public Object drop() {
        this.sqlBuilder.DROP();
        return this;
    }

    @Override
    public Object key() {
        this.sqlBuilder.KEY();
        return this;
    }

    @Override
    public Object primary() {
        this.sqlBuilder.PRIMARY();
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
        return this;
    }

    @Override
    public Object modify() {
        this.sqlBuilder.MODIFY();
        return this;
    }

    @Override
    public Object newColumn(Serializable field) {
        this.sqlBuilder.addWrapString(field.toString());
        return this;
    }

    @Override
    public Object oldColumn(Serializable field) {
        this.sqlBuilder.addWrapString(field.toString());
        return this;
    }

    @Override
    public Object rename() {
        this.sqlBuilder.RENAME();
        return this;
    }
}
