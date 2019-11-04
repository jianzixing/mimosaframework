package org.mimosaframework.orm.sql;

public class FieldBuilder {
    private Object field;
    private String aliasName;

    public FieldBuilder(Object field, String aliasName) {
        this.field = field;
        this.aliasName = aliasName;
    }

    public Object getField() {
        return field;
    }

    public String getAliasName() {
        return aliasName;
    }

    public static FieldBuilder builder(Object field, String aliasName) {
        return new FieldBuilder(field, aliasName);
    }
}
