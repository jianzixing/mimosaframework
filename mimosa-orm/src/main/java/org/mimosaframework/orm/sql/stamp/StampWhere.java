package org.mimosaframework.orm.sql.stamp;

public class StampWhere {
    public StampWhere next;
    public KeyLogic nextLogic;

    public StampColumn column;
    public String operator;
    public StampColumn compareColumn;
    public Object value;

    public StampWhere wrapWhere;
}
