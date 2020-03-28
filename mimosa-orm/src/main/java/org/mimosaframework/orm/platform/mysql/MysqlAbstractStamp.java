package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.sql.stamp.StampColumn;
import org.mimosaframework.orm.sql.stamp.StampTables;

public abstract class MysqlAbstractStamp {
    protected static final String RS = "`";
    protected static final String RE = "`";

    protected String getTableName(MappingGlobalWrapper wrapper,
                                  Class table,
                                  String tableName) {
        if (table != null) {
            MappingTable mappingTable = wrapper.getMappingTable(table);
            if (mappingTable != null) {
                return mappingTable.getMappingTableName();
            }
        } else {
            return tableName;
        }
        return null;
    }

    protected String getColumnName(MappingGlobalWrapper wrapper, StampTables stampTables, StampColumn column) {
        if (column != null && column.column != null) {
            if (column.table != null) {
                MappingTable mappingTable = wrapper.getMappingTable(column.table);
                if (mappingTable != null) {
                    MappingField mappingField = mappingTable.getMappingFieldByName(column.column.toString());
                    if (mappingField != null) {
                        return mappingField.getMappingColumnName();
                    }
                }
            }

            Class[] tables = stampTables.getTables();
            if (tables != null) {
                for (Class table : tables) {
                    MappingTable mappingTable = wrapper.getMappingTable(table);
                    if (mappingTable != null) {
                        MappingField mappingField = mappingTable.getMappingFieldByName(column.column.toString());
                        if (mappingField != null) {
                            return mappingField.getMappingColumnName();
                        }
                    }
                }
            }

            return column.column.toString();
        }
        return null;
    }
}
