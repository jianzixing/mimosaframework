package org.mimosaframework.orm.sql;

import java.io.Serializable;

public class FieldItem implements Serializable {
    private Class table;
    private String tableAliasName;
    private Serializable field;
    private boolean distinct = false;

    public FieldItem(Serializable field) {
        this.field = field;
    }

    public FieldItem(Class table, Serializable field) {
        this.table = table;
        this.field = field;
    }

    public FieldItem(String tableAliasName, Serializable field) {
        this.tableAliasName = tableAliasName;
        this.field = field;
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

    public Serializable getField() {
        return field;
    }

    public void setField(Serializable field) {
        this.field = field;
    }

    public void distinct() {
        this.distinct = true;
    }

    public boolean isDistinct() {
        return this.distinct;
    }
}
