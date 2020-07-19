package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampColumn;

import java.util.List;

public class PlatformStampReference {
    protected String RS = "\"";
    protected String RE = "\"";

    public String getTableName(MappingGlobalWrapper wrapper,
                               Class table,
                               String tableName) {
        if (table != null) {
            MappingTable mappingTable = wrapper.getMappingTable(table);
            if (mappingTable != null) {
                return mappingTable.getMappingTableName().toUpperCase();
            } else if (StringTools.isNotEmpty(tableName)) {
                return tableName.toUpperCase();
            }
        } else if (StringTools.isNotEmpty(tableName)) {
            return tableName.toUpperCase();
        }
        return null;
    }

    public String getColumnName(MappingGlobalWrapper wrapper, StampAction stampTables, StampColumn column) {
        return this.getColumnName(wrapper, stampTables, column, true);
    }

    public String getColumnName(MappingGlobalWrapper wrapper,
                                StampAction stampTables,
                                StampColumn column,
                                boolean hasRes) {
        String RS = this.RS, RE = this.RE;
        if (!hasRes) {
            RS = "";
            RE = "";
        }
        if (column != null && column.column != null) {
            String columnName = column.column.toString();
            String tableAliasName = column.tableAliasName;

            if (columnName.equals("*")) {
                if (StringTools.isNotEmpty(tableAliasName)) {
                    return tableAliasName.toUpperCase() + "." + columnName;
                } else {
                    return columnName;
                }
            }

            List<StampAction.STItem> tables = stampTables.getTables();
            if (tables != null && StringTools.isNotEmpty(tableAliasName)) {
                for (StampAction.STItem stItem : tables) {
                    if (tableAliasName.equals(stItem.getTableAliasName())) {
                        MappingTable mappingTable = wrapper.getMappingTable(stItem.getTable());
                        if (mappingTable != null) {
                            MappingField mappingField = mappingTable.getMappingFieldByName(columnName);
                            if (mappingField != null) {
                                return tableAliasName.toUpperCase() + "." + RS + mappingField.getMappingColumnName() + RE;
                            }
                        }
                    }
                }
            }

            if (column.table != null) {
                MappingTable mappingTable = wrapper.getMappingTable(column.table);
                if (mappingTable != null) {
                    MappingField mappingField = mappingTable.getMappingFieldByName(columnName);
                    if (mappingField != null) {
                        return mappingTable.getMappingTableName().toUpperCase()
                                + "."
                                + RS + mappingField.getMappingColumnName() + RE;
                    } else {
                        return mappingTable.getMappingTableName().toUpperCase()
                                + "."
                                + RS + columnName + RE;
                    }
                }
            }

            if (tables != null) {
                for (StampAction.STItem stItem : tables) {
                    MappingTable mappingTable = wrapper.getMappingTable(stItem.getTable());
                    if (mappingTable != null) {
                        MappingField mappingField = mappingTable.getMappingFieldByName(columnName);
                        if (mappingField != null) {
                            return RS + mappingField.getMappingColumnName() + RE;
                        }
                    }
                }
            }

            if (StringTools.isNotEmpty(tableAliasName)) {
                return tableAliasName.toUpperCase() + "." + RS + columnName + RE;
            } else {
                return RS + columnName + RE;
            }
        }
        return null;
    }

    public String getWrapStart() {
        return this.RS;
    }

    public String getWrapEnd() {
        return this.RE;
    }
}
