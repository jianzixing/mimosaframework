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
    public Object create() {
        this.addPoint("create");
        return this;
    }

    @Override
    public Object database() {
        this.addPoint("database");
        stampCreate.target = KeyTarget.DATABASE;
        return this;
    }

    @Override
    public Object name(Serializable value) {
        this.gammars.add("name");
        if (this.previous("index") || this.previous("unique")) {
            stampCreate.indexName = value.toString();
        } else {
            stampCreate.name = value.toString();
        }
        return this;
    }

    @Override
    public Object name(Class table) {
        this.gammars.add("name");
        stampCreate.table = table;
        return this;
    }

    @Override
    public Object charset(String charset) {
        this.gammars.add("charset");
        stampCreate.charset = charset;
        return this;
    }

    @Override
    public Object collate(String collate) {
        this.gammars.add("collate");
        stampCreate.collate = collate;
        return this;
    }

    @Override
    public Object ifNotExist() {
        this.gammars.add("ifNotExist");
        stampCreate.checkExist = true;
        return this;
    }

    @Override
    public Object extra(String sql) {
        this.gammars.add("extra");
        stampCreate.extra = sql;
        return this;
    }

    @Override
    public Object table() {
        this.addPoint("table");
        stampCreate.target = KeyTarget.TABLE;
        return this;
    }

    @Override
    public Object table(Class table) {
        this.gammars.add("table");
        stampCreate.table = table;
        return this;
    }

    @Override
    public Object index() {
        this.addPoint("index");
        stampCreate.target = KeyTarget.INDEX;
        return this;
    }

    @Override
    public Object on() {
        this.gammars.add("on");
        return this;
    }

    @Override
    public Object unique() {
        this.addPoint("unique");
        stampCreate.indexType = KeyIndexType.UNIQUE;
        return this;
    }

    @Override
    public Object columns(Serializable... columns) {
        this.gammars.add("columns");
        StampColumn[] stampColumns = new StampColumn[columns.length];
        for (int i = 0; i < columns.length; i++) stampColumns[i] = new StampColumn(columns[i]);
        stampCreate.indexColumns = stampColumns;
        return this;
    }


    @Override
    public Object column(Serializable field) {
        this.gammars.add("column");
        if (this.points != null && this.points.get(1).equals("table")) {
            StampCreateColumn column = new StampCreateColumn();
            column.column = new StampColumn(field);
            this.stampCreateColumns.add(column);
        }
        return this;
    }

    @Override
    public Object autoIncrement() {
        this.gammars.add("autoIncrement");
        StampCreateColumn column = this.getLastColumn();
        column.autoIncrement = true;
        return this;
    }

    @Override
    public Object comment(String comment) {
        this.gammars.add("comment");
        StampCreateColumn column = this.getLastColumn();
        column.comment = comment;
        return this;
    }

    @Override
    public Object defaultValue(String value) {
        this.gammars.add("defaultValue");
        StampCreateColumn column = this.getLastColumn();
        column.defaultValue = value;
        return this;
    }

    @Override
    public Object key() {
        this.gammars.add("key");
        StampCreateColumn column = this.getLastColumn();
        column.key = true;
        if (this.previous("primary")) {
            column.pk = true;
        }
        return this;
    }

    @Override
    public Object not() {
        this.gammars.add("not");
        return this;
    }

    @Override
    public Object nullable() {
        this.gammars.add("nullable");
        if (this.previous("not")) {
            StampCreateColumn column = this.getLastColumn();
            column.nullable = false;
        }
        return this;
    }

    @Override
    public Object primary() {
        this.gammars.add("primary");
        return this;
    }

    @Override
    public Object intType() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.INT;
        return this;
    }

    @Override
    public Object varchar(int len) {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.VARCHAR;
        column.len = len;
        return this;
    }

    @Override
    public Object charType(int len) {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.CHAR;
        column.len = len;
        return this;
    }

    @Override
    public Object blob() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.BLOB;
        return this;
    }

    @Override
    public Object text() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.TEXT;
        return this;
    }

    @Override
    public Object tinyint() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.TINYINT;
        return this;
    }

    @Override
    public Object smallint() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.SMALLINT;
        return this;
    }

    @Override
    public Object mediumint() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.MEDIUMINT;
        return this;
    }

    @Override
    public Object bit() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.BIT;
        return this;
    }

    @Override
    public Object bigint() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.BIGINT;
        return this;
    }

    @Override
    public Object floatType() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.FLOAT;
        return this;
    }

    @Override
    public Object doubleType() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.DOUBLE;
        return this;
    }

    @Override
    public Object decimal(int len, int scale) {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.DECIMAL;
        column.len = len;
        column.scale = scale;
        return this;
    }

    @Override
    public Object booleanType() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.BOOLEAN;
        return this;
    }

    @Override
    public Object date() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.DATE;
        return this;
    }

    @Override
    public Object time() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.TIME;
        return this;
    }

    @Override
    public Object datetime() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.DATETIME;
        return this;
    }

    @Override
    public Object timestamp() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.TIMESTAMP;
        return this;
    }

    @Override
    public Object year() {
        this.gammars.add("type");
        StampCreateColumn column = this.getLastColumn();
        column.columnType = KeyColumnType.YEAR;
        return this;
    }

    @Override
    public StampAction compile() {
        if (this.stampCreateColumns != null && this.stampCreateColumns.size() > 0) {
            this.stampCreate.columns = this.stampCreateColumns.toArray(new StampCreateColumn[]{});
        }
        return this.stampCreate;
    }

    @Override
    public Object tableComment(String comment) {
        this.stampCreate.comment = comment;
        return this;
    }
}
