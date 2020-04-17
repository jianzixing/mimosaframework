package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.AbstractSQLBuilder;
import org.mimosaframework.orm.sql.stamp.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultSQLCreateBuilder
        extends
        AbstractSQLBuilder
        implements
        RedefineCreateBuilder {

    protected StampCreate stampCreate = new StampCreate();
    protected List<StampCreateColumn> stampCreateColumns = new ArrayList<>();

    protected StampCreateColumn getLastColumn() {
        if (stampCreateColumns.size() > 0) {
            return stampCreateColumns.get(stampCreateColumns.size() - 1);
        }
        return null;
    }

    @Override
    public DefaultSQLCreateBuilder create() {
        this.addPoint("create");
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder database() {
        this.addPoint("database");
        stampCreate.target = KeyTarget.DATABASE;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder name(Serializable value) {
        this.gammars.add("name");
        if (this.previous("index") || this.previous("unique")) {
            stampCreate.indexName = value.toString();
        } else if (this.point.equals("database")) {
            stampCreate.databaseName = value.toString();
        } else {
            stampCreate.tableName = value.toString();
        }
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder name(Class table) {
        this.gammars.add("name");
        stampCreate.tableClass = table;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder charset(String charset) {
        this.gammars.add("charset");
        stampCreate.charset = charset;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder collate(String collate) {
        this.gammars.add("collate");
        stampCreate.collate = collate;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder ifNotExist() {
        this.gammars.add("ifNotExist");
        stampCreate.checkExist = true;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder extra(String sql) {
        this.gammars.add("extra");
        stampCreate.extra = sql;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder table() {
        this.addPoint("table");
        stampCreate.target = KeyTarget.TABLE;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder table(Class table) {
        this.gammars.add("table");
        stampCreate.tableClass = table;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder index() {
        this.addPoint("index");
        stampCreate.target = KeyTarget.INDEX;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder on() {
        this.gammars.add("on");
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder columns(Serializable... columns) {
        this.gammars.add("columns");
        StampColumn[] stampColumns = new StampColumn[columns.length];
        for (int i = 0; i < columns.length; i++) stampColumns[i] = new StampColumn(columns[i]);
        stampCreate.indexColumns = stampColumns;
        return this;
    }


    @Override
    public DefaultSQLCreateBuilder column(Serializable field) {
        this.gammars.add("column");
        if (this.points != null && this.points.get(1).equals("table")) {
            StampCreateColumn column = new StampCreateColumn();
            column.column = new StampColumn(field);
            this.stampCreateColumns.add(column);
        }
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder autoIncrement() {
        this.gammars.add("autoIncrement");
        StampCreateColumn column = this.getLastColumn();
        column.autoIncrement = KeyConfirm.YES;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder comment(String comment) {
        this.gammars.add("comment");
        StampCreateColumn column = this.getLastColumn();
        column.comment = comment;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder defaultValue(String value) {
        this.gammars.add("defaultValue");
        StampCreateColumn column = this.getLastColumn();
        column.defaultValue = value;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder key() {
        this.gammars.add("key");
        StampCreateColumn column = this.getLastColumn();
        if (this.previous("primary")) {
            column.pk = KeyConfirm.YES;
        }
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder not() {
        this.gammars.add("not");
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder nullable() {
        this.gammars.add("nullable");
        if (this.previous("not")) {
            StampCreateColumn column = this.getLastColumn();
            column.nullable = KeyConfirm.NO;
        }
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder primary() {
        this.gammars.add("primary");
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder intType() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.INT;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder varchar(int len) {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.VARCHAR;
        column.len = len;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder charType(int len) {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.CHAR;
        column.len = len;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder blob() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.BLOB;
        return this;
    }

    @Override
    public Object mediumBlob() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.MEDIUMBLOB;
        return this;
    }

    @Override
    public Object longBlob() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.LONGBLOB;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder text() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.TEXT;
        return this;
    }

    @Override
    public Object mediumText() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.MEDIUMTEXT;
        return this;
    }

    @Override
    public Object longText() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.LONGTEXT;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder tinyint() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.TINYINT;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder smallint() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.SMALLINT;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder bigint() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.BIGINT;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder floatType() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.FLOAT;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder doubleType() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.DOUBLE;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder decimal(int len, int scale) {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.DECIMAL;
        column.len = len;
        column.scale = scale;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder booleanType() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.BOOLEAN;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder date() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.DATE;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder time() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.TIME;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder datetime() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.DATETIME;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder timestamp() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.TIMESTAMP;
        return this;
    }

    @Override
    public StampCreate compile() {
        if (this.stampCreateColumns != null && this.stampCreateColumns.size() > 0) {
            this.stampCreate.columns = this.stampCreateColumns.toArray(new StampCreateColumn[]{});
        }
        return this.stampCreate;
    }

    @Override
    public DefaultSQLCreateBuilder tableComment(String comment) {
        this.stampCreate.comment = comment;
        return this;
    }

    @Override
    public DefaultSQLCreateBuilder table(String name) {
        this.gammars.add("table");
        if (this.point.equals("index")) {
            this.stampCreate.tableName = name;
        }
        return this;
    }

    public DefaultSQLCreateBuilder timeForUpdate() {
        StampCreateColumn column = this.getLastColumn();
        if (column != null) {
            column.timeForUpdate = true;
        }
        return this;
    }
}
