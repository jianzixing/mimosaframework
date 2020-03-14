package org.mimosaframework.orm.sql.test;

public class FromItem {
    private Class table;
    private String aliasName;

    public FromItem(Class table) {
        this.table = table;
    }

    public FromItem(Class table, String aliasName) {
        this.table = table;
        this.aliasName = aliasName;
    }

    public static FromItem create(Class table) {
        return new FromItem(table);
    }

    public static FromItem create(Class table, String aliasName) {
        return new FromItem(table, aliasName);
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
