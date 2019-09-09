package org.mimosaframework.orm.mapping;

public class SupposedTables {
    private String tableName;
    private Class tableClass;
    private boolean isSplitTable = false;
    private String splitName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Class getTableClass() {
        return tableClass;
    }

    public void setTableClass(Class tableClass) {
        this.tableClass = tableClass;
    }

    public boolean isSplitTable() {
        return isSplitTable;
    }

    public String getSplitName() {
        return splitName;
    }

    public void setSplitTable(boolean splitTable) {
        isSplitTable = splitTable;
    }

    public void setSplitName(String splitName) {
        this.splitName = splitName;
    }
}
