package org.mimosaframework.orm.sql.stamp;

public class StampAlterItem extends StampTableColumn {
    public KeyAction action;
    public KeyAlterStruct struct;

    public StampColumn after;
    public StampColumn before;
    public boolean first;

    // primary key name
    public String indexName;

    // add columns
    public StampColumn[] columns;

    // column change && column rename
    public StampColumn oldColumn;

    // drop
    public KeyAlterDropType dropType;

    // set primary key = ?
    public Object value;

    // set charset
    public String charset;
}
