package org.mimosaframework.orm.sql.test;

public class FieldItem {
    private Class table;
    private String tableAliasName;
    private Object field;
    private String fieldAliasName;

    public FieldItem(Class table, Object field) {
        this.table = table;
        this.field = field;
    }

    public FieldItem(String tableAliasName, Object field) {
        this.tableAliasName = tableAliasName;
        this.field = field;
    }

    public FieldItem(Class table, Object field, String fieldAliasName) {
        this.table = table;
        this.field = field;
        this.fieldAliasName = fieldAliasName;
    }

    public FieldItem(String tableAliasName, Object field, String fieldAliasName) {
        this.tableAliasName = tableAliasName;
        this.field = field;
        this.fieldAliasName = fieldAliasName;
    }

    public Class getTable() {
        return table;
    }

    public void setTable(Class table) {
        this.table = table;
    }

    public String getTableAliasName() {
        return tableAliasName;
    }

    public void setTableAliasName(String tableAliasName) {
        this.tableAliasName = tableAliasName;
    }

    public Object getField() {
        return field;
    }

    public void setField(Object field) {
        this.field = field;
    }
}
