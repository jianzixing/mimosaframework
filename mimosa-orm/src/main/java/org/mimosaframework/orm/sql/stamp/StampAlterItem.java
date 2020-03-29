package org.mimosaframework.orm.sql.stamp;

public class StampAlterItem {
    public KeyAction action;
    public KeyAlterStruct struct;

    // column & drop column
    public StampColumn column;
    public KeyColumnType columnType;
    public int len;
    public int scale;
    public boolean nullable = true;
    public String defaultValue;
    public boolean autoIncrement = false;
    public boolean unique = false;
    public boolean pk = false;
    public boolean key = false;
    public String comment;

    public StampColumn after;
    public StampColumn before;

    // add columns
    public StampColumn[] columns;

    // index
    public KeyIndexType indexType;
    // index && rename
    public String name;

    // column change && column rename
    public StampColumn oldColumn;

    // drop
    public KeyAlterDropType dropType;

    // rename
    public KeyAlterRenameType renameType;
    public String oldName;

    // set primary key = ?
    public Object value;
}
