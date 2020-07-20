package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampColumn;

import java.util.List;

public class PlatformStampReference {
    public String getTableName(MappingGlobalWrapper wrapper,
                               Class table,
                               String tableName) {
        return this.getTableName(wrapper, table, tableName, true);
    }

    public String getTableName(MappingGlobalWrapper wrapper,
                               Class table,
                               String tableName,
                               boolean hasRes) {
        boolean isUpperCase = this.isUpperCase();
        String RS = !hasRes ? "" : this.getWrapStart();
        String RE = !hasRes ? "" : this.getWrapEnd();

        if (table != null) {
            MappingTable mappingTable = wrapper.getMappingTable(table);
            if (isUpperCase) {
                if (mappingTable != null) {
                    return mappingTable.getMappingTableName().toUpperCase();
                } else if (StringTools.isNotEmpty(tableName)) {
                    return tableName.toUpperCase();
                }
            } else {
                if (mappingTable != null) {
                    return RS + mappingTable.getMappingTableName() + RE;
                } else {
                    return RS + tableName + RE;
                }
            }
        } else if (StringTools.isNotEmpty(tableName)) {
            if (isUpperCase) {
                return (hasRes ? RS : "") + tableName.toUpperCase() + (hasRes ? RE : "");
            } else {
                return RS + tableName + RE;
            }
        }
        return null;
    }

    public String getColumnName(MappingGlobalWrapper wrapper, StampAction stampTables, StampColumn column) {
        return this.getColumnName(wrapper, stampTables, column, true);
    }

    /**
     * oracle 的列名不区分大小写，但是如果被双引号引用的则区分大小写
     * 比如
     * id 和 ID 一致
     * "id" 和 "ID" 不一致
     * <p>
     * 如果列名不加双引号则创建表时数据库自动大写,加了双引号则为原始字符串
     *
     * @param wrapper
     * @param stampTables
     * @param column
     * @return
     */
    public String getColumnName(MappingGlobalWrapper wrapper,
                                StampAction stampTables,
                                StampColumn column,
                                boolean hasRes) {
        boolean isUpperCase = this.isUpperCase();
        String RS = !hasRes ? "" : this.getWrapStart();
        String RE = !hasRes ? "" : this.getWrapEnd();

        if (column != null && column.column != null) {
            String columnName = column.column.toString();
            String tableAliasName = column.tableAliasName;

            if (columnName.equals("*")) {
                if (StringTools.isNotEmpty(tableAliasName)) {
                    if (isUpperCase) {
                        return tableAliasName.toUpperCase() + "." + columnName;
                    } else {
                        return RS + tableAliasName + RE + "." + columnName;
                    }
                } else {
                    return columnName;
                }
            }

            List<StampAction.STItem> tables = stampTables.getTables();
            if (StringTools.isNotEmpty(tableAliasName)) {
                if (tables != null) {
                    for (StampAction.STItem stItem : tables) {
                        if (tableAliasName.equals(stItem.getTableAliasName())) {
                            MappingTable mappingTable = wrapper.getMappingTable(stItem.getTable());
                            if (mappingTable != null) {
                                MappingField mappingField = mappingTable.getMappingFieldByName(columnName);
                                if (mappingField != null) {
                                    if (isUpperCase) {
                                        return tableAliasName.toUpperCase() + "." + RS + mappingField.getMappingColumnName() + RE;
                                    } else {
                                        return RS + tableAliasName + RE + "." + RS + mappingField.getMappingColumnName() + RE;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    return RS + tableAliasName + RE + "." + RS + columnName + RE;
                }
            }

            if (column.table != null) {
                MappingTable mappingTable = wrapper.getMappingTable(column.table);
                if (mappingTable != null) {
                    MappingField mappingField = mappingTable.getMappingFieldByName(columnName);
                    if (mappingField != null) {
                        if (isUpperCase) {
                            return mappingTable.getMappingTableName().toUpperCase()
                                    + "."
                                    + RS + mappingField.getMappingColumnName() + RE;
                        } else {
                            return RS + mappingTable.getMappingTableName() + RE
                                    + "."
                                    + RS + mappingField.getMappingColumnName() + RE;
                        }
                    } else {
                        if (isUpperCase) {
                            return mappingTable.getMappingTableName().toUpperCase()
                                    + "."
                                    + RS + columnName + RE;
                        } else {
                            return mappingTable.getMappingTableName()
                                    + "."
                                    + RS + columnName + RE;
                        }
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
                if (isUpperCase) {
                    return tableAliasName.toUpperCase() + "." + RS + columnName + RE;
                } else {
                    return tableAliasName + "." + RS + columnName + RE;
                }
            } else {
                return RS + columnName + RE;
            }
        }
        return null;
    }

    public String getWrapStart() {
        return "\"";
    }

    public String getWrapEnd() {
        return "\"";
    }

    protected boolean isUpperCase() {
        return false;
    }
}
