package org.mimosaframework.orm.sql.stamp;

public class StampHaving {
    public StampHaving next;
    public KeyLogic nextLogic;

    public StampColumn column;
    public String operator;
    public StampColumn compareColumn;
    public StampSelectFieldFun compareFun;
    public Object value;

    public StampHaving wrapWhere;
}
