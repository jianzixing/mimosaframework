package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;

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

    public String getDatabaseTableName(MappingGlobalWrapper mappingGlobalWrapper) {
        MappingTable mappingTable = mappingGlobalWrapper.getMappingTable(table);
        if (mappingTable != null) {
            return mappingTable.getMappingTableName();
        }
        return null;
    }
}
