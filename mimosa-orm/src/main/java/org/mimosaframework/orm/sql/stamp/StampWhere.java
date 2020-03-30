package org.mimosaframework.orm.sql.stamp;

public class StampWhere {
    public StampWhere next;
    public KeyLogic nextLogic;

    // c = 1 or c = b
    public StampColumn column;              // c
    public String operator;                 // =
    public StampColumn compareColumn;       // b
    public Object value;                    // 1
    public Object value2;                   // between A B

    public StampWhere wrapWhere;            // (a = b ... )

    // count(a)>0 or isNull(b)
    public StampFieldFun fun;         // count(a)>0
    public StampFieldFun compareFun;  // isNull(b)
}
