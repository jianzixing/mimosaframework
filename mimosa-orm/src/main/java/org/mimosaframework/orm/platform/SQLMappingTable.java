package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;

public class SQLMappingTable {
    private Class table;
    private String tableAliasName;

    public SQLMappingTable(Class table) {
        this.table = table;
    }

    public SQLMappingTable(Class table, String tableAliasName) {
        this.table = table;
        this.tableAliasName = tableAliasName;
    }

    public Class getTable() {
        return table;
    }

    public void setTable(Class table) {
        this.table = table;
    }

    public String getDatabaseTableName(MappingGlobalWrapper mappingGlobalWrapper) {
        MappingTable mappingTable = mappingGlobalWrapper.getMappingTable(table);
        if (mappingTable != null) {
            return mappingTable.getMappingTableName();
        }
        return null;
    }

    public String getTableAliasName() {
        return tableAliasName;
    }

    public void setTableAliasName(String tableAliasName) {
        this.tableAliasName = tableAliasName;
    }
}
