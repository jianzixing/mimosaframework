package org.mimosaframework.orm.sql.stamp;

public class StampAlterItem extends StampTableColumn {
    public KeyAction action;
    public KeyAlterStruct struct;

    public StampColumn after;
    public StampColumn before;

    // add columns
    public StampColumn[] columns;

    // index
    public KeyIndexType indexType;
    // index
    public String indexName;

    // column change && column rename
    public StampColumn oldColumn;

    // drop
    public KeyAlterDropType dropType;

    // set primary key = ?
    public Object value;

    // set charset
    public String charset;
}
