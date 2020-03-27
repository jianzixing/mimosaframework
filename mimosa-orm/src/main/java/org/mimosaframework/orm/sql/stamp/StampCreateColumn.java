package org.mimosaframework.orm.sql.stamp;

public class StampCreateColumn {
    public StampColumn column;
    public KeyColumnType columnType;
    public boolean nullable = true;
    public String defaultValue;
    public boolean autoIncrement = false;
    public boolean unique = false;
    public boolean pk = false;
    public boolean key = false;
    public String comment;
}
