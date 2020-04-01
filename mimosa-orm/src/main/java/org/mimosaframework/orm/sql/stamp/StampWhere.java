package org.mimosaframework.orm.sql.stamp;

public class StampWhere {
    public KeyWhereType whereType;

    public StampWhere next;
    public KeyLogic nextLogic;

    public StampColumn leftColumn;
    public StampFieldFun leftFun;
    public Object leftValue;

    public String operator;
    public boolean not = false;

    public StampColumn rightColumn;
    public StampFieldFun rightFun;
    public Object rightValue;
    public Object rightValueEnd;

    public StampFieldFun fun;           // isNull(b)

    public StampWhere wrapWhere;        // (a = b ... )
}
