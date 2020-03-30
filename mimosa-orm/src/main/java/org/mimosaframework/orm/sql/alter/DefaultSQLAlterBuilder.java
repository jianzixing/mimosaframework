package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.AbstractSQLBuilder;
import org.mimosaframework.orm.sql.stamp.*;

import java.io.Serializable;
import java.util.List;

public class DefaultSQLAlterBuilder
        extends
        AbstractSQLBuilder
        implements
        RedefineAlterBuilder {

    protected StampAlter stampAlter = new StampAlter();
    protected List<StampAlterItem> lastItems = null;

    private StampAlterItem getItem() {
        StampAlterItem item = this.lastItems.get(this.lastItems.size() - 1);
        return item;
    }

    @Override
    public Object alter() {
        this.gammars.add("alter");
        return this;
    }

    @Override
    public Object name(Serializable value) {
        this.gammars.add("name");
        this.stampAlter.name = value.toString();
        return this;
    }

    @Override
    public Object charset(String charset) {
        this.gammars.add("charset");
        this.stampAlter.charset = charset;
        return this;
    }

    @Override
    public Object collate(String collate) {
        this.gammars.add("collate");
        this.stampAlter.collate = collate;
        return this;
    }

    @Override
    public Object database() {
        this.gammars.add("database");
        this.stampAlter.target = KeyTarget.DATABASE;
        return this;
    }

    @Override
    public Object column(Serializable field) {
        this.gammars.add("column");
        StampAlterItem item = this.getItem();
        if (this.hasPreviousStop("add", "change", "modify", ",")) {
            item.after = new StampColumn(field);
        } else {
            item.column = new StampColumn(field);
            item.struct = KeyAlterStruct.COLUMN;

            if (this.hasPreviousStop("drop", ",")) {
                item.dropType = KeyAlterDropType.COLUMN;
            }
        }
        return this;
    }

    @Override
    public Object columns(Serializable... fields) {
        this.gammars.add("columns");
        StampColumn[] columns = new StampColumn[fields.length];
        int i = 0;
        for (Serializable field : fields) {
            columns[i] = new StampColumn(field);
            i++;
        }
        StampAlterItem item = this.getItem();
        item.columns = columns;
        return this;
    }

    @Override
    public Object table(Class table) {
        this.gammars.add("table");
        this.stampAlter.target = KeyTarget.TABLE;
        this.stampAlter.table = table;
        return this;
    }

    @Override
    public Object after() {
        this.gammars.add("after");
        return this;
    }

    @Override
    public Object autoIncrement() {
        this.gammars.add("autoIncrement");
        StampAlterItem item = this.getItem();
        item.autoIncrement = true;
        return this;
    }

    @Override
    public Object column() {
        this.gammars.add("column");
        return this;
    }

    @Override
    public Object add() {
        this.gammars.add("add");
        StampAlterItem item = new StampAlterItem();
        lastItems.add(item);
        return this;
    }

    @Override
    public Object intType() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.INT;
        return this;
    }

    @Override
    public Object varchar(int len) {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.VARCHAR;
        item.len = len;
        return this;
    }

    @Override
    public Object charType(int len) {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.CHAR;
        item.len = len;
        return this;
    }

    @Override
    public Object blob() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.BLOB;
        return this;
    }

    @Override
    public Object text() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.TEXT;
        return this;
    }

    @Override
    public Object tinyint() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.TINYINT;
        return this;
    }

    @Override
    public Object smallint() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.SMALLINT;
        return this;
    }

    @Override
    public Object mediumint() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.MEDIUMINT;
        return this;
    }

    @Override
    public Object bit() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.BIT;
        return this;
    }

    @Override
    public Object bigint() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.BIGINT;
        return this;
    }

    @Override
    public Object floatType() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.FLOAT;
        return this;
    }

    @Override
    public Object doubleType() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.DOUBLE;
        return this;
    }

    @Override
    public Object decimal(int len, int scale) {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.DECIMAL;
        item.len = len;
        item.scale = scale;
        return this;
    }

    @Override
    public Object booleanType() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.DOUBLE;
        return this;
    }

    @Override
    public Object date() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.DATE;
        return this;
    }

    @Override
    public Object time() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.TIME;
        return this;
    }

    @Override
    public Object datetime() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.DATETIME;
        return this;
    }

    @Override
    public Object timestamp() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.TIMESTAMP;
        return this;
    }

    @Override
    public Object year() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.YEAR;
        return this;
    }

    @Override
    public Object comment(String comment) {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.comment = comment;
        return this;
    }

    @Override
    public Object index() {
        this.gammars.add("type");
        StampAlterItem item = this.getItem();
        item.struct = KeyAlterStruct.INDEX;
        if (this.hasPreviousStop("drop", ",")) {
            item.dropType = KeyAlterDropType.INDEX;
        }
        return this;
    }

    @Override
    public Object split() {
        this.gammars.add(",");
        return this;
    }

    @Override
    public Object drop() {
        this.gammars.add("drop");
        StampAlterItem item = new StampAlterItem();
        lastItems.add(item);
        return this;
    }

    @Override
    public Object key() {
        this.gammars.add("key");
        if (this.previous("primary")) {
            StampAlterItem item = this.getItem();
            item.pk = true;
            if (this.hasPreviousStop("drop", ",")) {
                item.dropType = KeyAlterDropType.PRIMARY_KEY;
            }
        } else {
            StampAlterItem item = this.getItem();
            item.key = true;
        }
        return this;
    }

    @Override
    public Object primary() {
        this.gammars.add("primary");
        return this;
    }

    @Override
    public Object to() {
        this.gammars.add("to");
        return this;
    }

    @Override
    public Object change() {
        this.gammars.add("change");
        StampAlterItem item = new StampAlterItem();
        lastItems.add(item);
        return this;
    }

    @Override
    public Object modify() {
        this.gammars.add("modify");
        StampAlterItem item = new StampAlterItem();
        lastItems.add(item);
        return this;
    }

    @Override
    public Object newColumn(Serializable field) {
        this.gammars.add("column");
        StampAlterItem item = this.getItem();
        item.column = new StampColumn(field);
        return this;
    }

    @Override
    public Object oldColumn(Serializable field) {
        this.gammars.add("column");
        StampAlterItem item = this.getItem();
        item.oldColumn = new StampColumn(field);
        return this;
    }

    @Override
    public Object rename() {
        this.gammars.add("rename");
        StampAlterItem item = new StampAlterItem();
        lastItems.add(item);
        return this;
    }

    @Override
    public Object fullText() {
        this.gammars.add("fullText");
        StampAlterItem item = this.getItem();
        item.indexType = KeyIndexType.FULLTEXT;
        return this;
    }

    @Override
    public Object unique() {
        this.gammars.add("unique");
        StampAlterItem item = this.getItem();
        item.indexType = KeyIndexType.UNIQUE;
        return this;
    }

    @Override
    public Object value(int number) {
        this.gammars.add("value");
        return this;
    }

    @Override
    public StampAction compile() {
        this.gammars.add("compile");
        return this.stampAlter;
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
            StampAlterItem item = this.getItem();
            item.nullable = false;
        }
        return this;
    }

    @Override
    public Object defaultValue(String value) {
        this.gammars.add("defaultValue");
        StampAlterItem item = this.getItem();
        item.defaultValue = value;
        return this;
    }
}
