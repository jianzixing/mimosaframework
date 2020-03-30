package org.mimosaframework.orm.sql.stamp;

public class StampFieldFun {
    public String funName;
    public Object[] params;

    public StampFieldFun(String funName, Object... params) {
        this.funName = funName;
        this.params = params;
    }
}
