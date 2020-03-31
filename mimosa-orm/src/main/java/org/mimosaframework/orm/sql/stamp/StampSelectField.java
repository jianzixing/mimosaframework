package org.mimosaframework.orm.sql.stamp;

public class StampSelectField {
    public KeyFieldType fieldType;
    public StampColumn column;
    public StampFieldFun fun;
    public boolean distinct = false;

    public String aliasName;
    public String tableAliasName;
}
