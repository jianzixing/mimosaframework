package org.mimosaframework.orm.sql.test;

public class FieldItem {
    private Object field;
    private String aliasName;

    public FieldItem(Object field) {
        this.field = field;
    }

    public FieldItem(String aliasName, Object field) {
        this.field = field;
        this.aliasName = aliasName;
    }

    public static FieldItem create(Object field) {
        return new FieldItem(field);
    }

    public static FieldItem create(String aliasName, Object field) {
        return new FieldItem(aliasName, field);
    }

    public Object getField() {
        return field;
    }

    public void setField(Object field) {
        this.field = field;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
}
