package org.mimosaframework.orm.platform;

public class SQLMappingTable {
    private Class table;

    public SQLMappingTable(Class table) {
        this.table = table;
    }

    public Class getTable() {
        return table;
    }

    public void setTable(Class table) {
        this.table = table;
    }
}
