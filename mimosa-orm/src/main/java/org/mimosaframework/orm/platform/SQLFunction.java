package org.mimosaframework.orm.platform;

public class SQLFunction {
    private String funName;
    private String tableAliasName;
    private Object field;
    private String fieldAliasName;
    private boolean isValue;

    public SQLFunction(String funName, Object field, String alias) {
        this.funName = funName;
        this.field = field;
        this.fieldAliasName = alias;
    }


    public SQLFunction(String funName, Object field, String alias, boolean isValue) {
        this.funName = funName;
        this.field = field;
        this.fieldAliasName = alias;
        this.isValue = isValue;
    }

    public SQLFunction(String funName, String tableAliasName, Object field, String fieldAliasName) {
        this.funName = funName;
        this.tableAliasName = tableAliasName;
        this.field = field;
        this.fieldAliasName = fieldAliasName;
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

    public String getTableAliasName() {
        return tableAliasName;
    }

    public void setTableAliasName(String tableAliasName) {
        this.tableAliasName = tableAliasName;
    }

    public String getFieldAliasName() {
        return fieldAliasName;
    }

    public void setFieldAliasName(String fieldAliasName) {
        this.fieldAliasName = fieldAliasName;
    }

    public boolean isValue() {
        return isValue;
    }

    public void setValue(boolean value) {
        isValue = value;
    }
}
