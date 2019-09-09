package org.mimosaframework.orm.platform;

public class SQLFunction {
    private String funName;
    private Object field;
    private String alias;

    public SQLFunction(String funName, Object field, String alias) {
        this.funName = funName;
        this.field = field;
        this.alias = alias;
    }

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public Object getField() {
        return field;
    }

    public void setField(Object field) {
        this.field = field;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
