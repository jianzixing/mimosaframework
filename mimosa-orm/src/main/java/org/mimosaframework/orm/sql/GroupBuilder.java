package org.mimosaframework.orm.sql;

public class GroupBuilder {
    private Class table;
    private Object field;

    public GroupBuilder(Object field) {
        this.field = field;
    }

    public GroupBuilder(Class table, Object field) {
        this.table = table;
        this.field = field;
    }

    public Class getTable() {
        return table;
    }

    public Object getField() {
        return field;
    }
}
