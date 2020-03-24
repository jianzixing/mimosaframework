package org.mimosaframework.orm.platform;

import java.io.Serializable;

public class SQLMappingField {
    private Class table;
    private String tableAliasName;
    private Serializable field;

    public SQLMappingField(Class table, Serializable field) {
        this.table = table;
        this.field = field;
    }

    public SQLMappingField(String tableAliasName, Serializable field) {
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
}
