package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.stamp.*;

import java.io.Serializable;
import java.util.List;

public class DefaultSQLAlterBuilder
        implements
        RedefineAlterBuilder {

    protected KeyTarget target;
    /**
     * 10 add ~
     * 11 change ~
     * 12 drop ~
     * 13 modify ~
     * 14 rename ~
     * <p>
     * 20 after ~
     * 21 before ~
     */
    protected int body = 0;
    protected boolean isNot = false;
    protected boolean isPrimary = false;
    protected KeyIndexType indexType;

    protected StampAlter stampAlter = new StampAlter();
    protected List<StampAlterItem> lastItems = null;

    private StampAlterItem getItem() {
        StampAlterItem item = this.lastItems.get(this.lastItems.size() - 1);
        return item;
    }

    @Override
    public Object alter() {
        return this;
    }

    @Override
    public Object name(Serializable value) {
        this.stampAlter.name = value.toString();
        return this;
    }

    @Override
    public Object charset(String charset) {
        this.stampAlter.charset = charset;
        return this;
    }

    @Override
    public Object collate(String collate) {
        this.stampAlter.collate = collate;
        return this;
    }

    @Override
    public Object database() {
        this.target = KeyTarget.DATABASE;
        this.stampAlter.target = this.target;
        return this;
    }

    @Override
    public Object column(Serializable field) {
        StampAlterItem item = this.getItem();
        if (this.body == 20 || this.body == 21) {
            item.after = new StampColumn(field);
        } else {
            item.column = new StampColumn(field);
            item.struct = KeyAlterStruct.COLUMN;

            if (this.body == 12) {
                item.dropType = KeyAlterDropType.COLUMN;
            }
        }
        return this;
    }

    @Override
    public Object columns(Serializable... fields) {
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
        this.target = KeyTarget.TABLE;
        this.stampAlter.target = this.target;
        this.stampAlter.table = table;
        return this;
    }

    @Override
    public Object after() {
        this.body = 20;
        return this;
    }

    @Override
    public Object autoIncrement() {
        StampAlterItem item = this.getItem();
        item.autoIncrement = true;
        return this;
    }

    @Override
    public Object column() {
        return this;
    }

    @Override
    public Object add() {
        StampAlterItem item = new StampAlterItem();
        lastItems.add(item);
        this.body = 10;
        return this;
    }

    @Override
    public Object intType() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.INT;
        return this;
    }

    @Override
    public Object varchar(int len) {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.VARCHAR;
        item.len = len;
        return this;
    }

    @Override
    public Object charType(int len) {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.CHAR;
        item.len = len;
        return this;
    }

    @Override
    public Object blob() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.BLOB;
        return this;
    }

    @Override
    public Object text() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.TEXT;
        return this;
    }

    @Override
    public Object tinyint() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.TINYINT;
        return this;
    }

    @Override
    public Object smallint() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.SMALLINT;
        return this;
    }

    @Override
    public Object mediumint() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.MEDIUMINT;
        return this;
    }

    @Override
    public Object bit() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.BIT;
        return this;
    }

    @Override
    public Object bigint() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.BIGINT;
        return this;
    }

    @Override
    public Object floatType() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.FLOAT;
        return this;
    }

    @Override
    public Object doubleType() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.DOUBLE;
        return this;
    }

    @Override
    public Object decimal(int len, int scale) {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.DECIMAL;
        item.len = len;
        item.scale = scale;
        return this;
    }

    @Override
    public Object booleanType() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.DOUBLE;
        return this;
    }

    @Override
    public Object date() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.DATE;
        return this;
    }

    @Override
    public Object time() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.TIME;
        return this;
    }

    @Override
    public Object datetime() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.DATETIME;
        return this;
    }

    @Override
    public Object timestamp() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.TIMESTAMP;
        return this;
    }

    @Override
    public Object year() {
        StampAlterItem item = this.getItem();
        item.columnType = KeyColumnType.YEAR;
        return this;
    }

    @Override
    public Object comment(String comment) {
        StampAlterItem item = this.getItem();
        item.comment = comment;
        return this;
    }

    @Override
    public Object index() {
        StampAlterItem item = this.getItem();
        item.struct = KeyAlterStruct.INDEX;
        if (this.body == 12) {
            item.dropType = KeyAlterDropType.INDEX;
        }
        return this;
    }

    @Override
    public Object split() {
        return this;
    }

    @Override
    public Object drop() {
        StampAlterItem item = new StampAlterItem();
        lastItems.add(item);
        this.body = 12;
        return this;
    }

    @Override
    public Object key() {
        if (isPrimary) {
            this.isPrimary = false;
            StampAlterItem item = this.getItem();
            item.pk = true;
            if (this.body == 12) {
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
        this.isPrimary = true;
        return this;
    }

    @Override
    public Object to() {
        return this;
    }

    @Override
    public Object change() {
        StampAlterItem item = new StampAlterItem();
        lastItems.add(item);
        this.body = 11;
        return this;
    }

    @Override
    public Object modify() {
        StampAlterItem item = new StampAlterItem();
        lastItems.add(item);
        this.body = 13;
        return this;
    }

    @Override
    public Object newColumn(Serializable field) {
        StampAlterItem item = this.getItem();
        item.column = new StampColumn(field);
        return this;
    }

    @Override
    public Object oldColumn(Serializable field) {
        StampAlterItem item = this.getItem();
        item.oldColumn = new StampColumn(field);
        return this;
    }

    @Override
    public Object rename() {
        StampAlterItem item = new StampAlterItem();
        lastItems.add(item);
        this.body = 14;
        return this;
    }

    @Override
    public Object fullText() {
        StampAlterItem item = this.getItem();
        item.indexType = KeyIndexType.FULLTEXT;
        return this;
    }

    @Override
    public Object unique() {
        StampAlterItem item = this.getItem();
        item.indexType = KeyIndexType.UNIQUE;
        return this;
    }

    @Override
    public Object value(int number) {
        return this;
    }

    @Override
    public StampAction compile() {
        return this.stampAlter;
    }

    @Override
    public Object not() {
        this.isNot = true;
        return this;
    }

    @Override
    public Object nullable() {
        if (this.isNot) {
            this.isNot = false;
            StampAlterItem item = this.getItem();
            item.nullable = false;
        }
        return this;
    }

    @Override
    public Object defaultValue(String value) {
        StampAlterItem item = this.getItem();
        item.defaultValue = value;
        return this;
    }
}
