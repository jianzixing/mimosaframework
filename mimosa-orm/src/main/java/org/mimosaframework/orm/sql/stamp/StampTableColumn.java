package org.mimosaframework.orm.sql.stamp;

public class StampTableColumn {
    public StampColumn column;
    public KeyColumnType columnType;
    public int len;
    public int scale;
    public KeyConfirm nullable;
    public String defaultValue;
    public KeyConfirm autoIncrement;
    public KeyConfirm pk;
    public String comment;

    public boolean timeForUpdate = false;
    public boolean timeForCreate = false;
}
