package org.mimosaframework.orm.sql.test;

public class TableItem {
    private Class table;
    private String aliasName;

    public TableItem(Class table) {
        this.table = table;
    }

    public TableItem(Class table, String aliasName) {
        this.table = table;
        this.aliasName = aliasName;
    }

    public Class getTable() {
        return table;
    }

    public void setTable(Class table) {
        this.table = table;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
}
