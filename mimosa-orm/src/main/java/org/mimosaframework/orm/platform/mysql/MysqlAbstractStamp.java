package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SQLDataPlaceholder;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.List;

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

    protected String getColumnName(MappingGlobalWrapper wrapper, StampAction stampTables, StampColumn column) {
        if (column != null && column.column != null) {
            String columnName = column.column.toString();
            String tableAliasName = column.tableAliasName;

            if (columnName.equals("*")) {
                if (StringTools.isNotEmpty(tableAliasName)) {
                    return RS + tableAliasName + RE + "." + columnName;
                } else {
                    return columnName;
                }
            }
            if (column.table != null) {
                MappingTable mappingTable = wrapper.getMappingTable(column.table);
                if (mappingTable != null) {
                    MappingField mappingField = mappingTable.getMappingFieldByName(columnName);
                    if (mappingField != null) {
                        return RS + mappingField.getMappingColumnName() + RE;
                    }
                }
            }

            List<StampAction.STItem> tables = stampTables.getTables();
            if (tables != null) {
                if (StringTools.isNotEmpty(tableAliasName)) {
                    for (StampAction.STItem stItem : tables) {
                        if (tableAliasName.equals(stItem.getTableAliasName())) {
                            MappingTable mappingTable = wrapper.getMappingTable(stItem.getTable());
                            if (mappingTable != null) {
                                MappingField mappingField = mappingTable.getMappingFieldByName(columnName);
                                if (mappingField != null) {
                                    return RS + tableAliasName + RE + "." + RS + mappingField.getMappingColumnName() + RE;
                                }
                            }
                        }
                    }
                }

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

            return columnName;
        }
        return null;
    }

    protected void buildWhere(MappingGlobalWrapper wrapper,
                              List<SQLDataPlaceholder> placeholders,
                              StampAction stampTables,
                              StampWhere where,
                              StringBuilder sb) {
        StampFieldFun fun = where.fun;
        StampColumn column = where.column;
        StampWhere next = where.next;
        StampWhere wrapWhere = where.wrapWhere;
        if (column != null) {
            String key = null;
            if (fun != null) {
                this.buildSelectFieldFun(wrapper, stampTables, fun, sb);
                key = "HAVING&" + fun.funName;
            } else {
                key = this.getColumnName(wrapper, stampTables, column);
                sb.append(key);
            }
            sb.append(" " + where.operator + " ");
            if (where.compareFun != null) {
                this.buildSelectFieldFun(wrapper, stampTables, fun, sb);
            } else if (where.compareColumn != null) {
                sb.append(this.getColumnName(wrapper, stampTables, where.compareColumn));
            } else if (where.value != null) {
                sb.append("?");
                SQLDataPlaceholder placeholder = new SQLDataPlaceholder();
                placeholder.setName(key);
                placeholder.setValue(where.value);
                placeholders.add(placeholder);
            }
        } else if (wrapWhere != null) {
            sb.append("(");
            this.buildWhere(wrapper, placeholders, stampTables, wrapWhere, sb);
            sb.append(")");
        }

        if (next != null) {
            if (where.nextLogic == KeyLogic.AND)
                sb.append(" AND ");
            else if (where.nextLogic == KeyLogic.OR)
                sb.append(" OR ");

            this.buildWhere(wrapper, placeholders, stampTables, next, sb);
        }
    }

    protected void buildSelectFieldFun(MappingGlobalWrapper wrapper,
                                       StampAction stampTables,
                                       StampFieldFun fun,
                                       StringBuilder sb) {
        String funName = fun.funName;
        Object[] params = fun.params;

        sb.append(funName);
        if (params != null) {
            sb.append("(");
            for (Object param : params) {
                if (param instanceof StampColumn) {
                    sb.append(this.getColumnName(wrapper, stampTables, (StampColumn) param));
                }
                if (param instanceof Number) {
                    sb.append(param);
                }
                if (param instanceof String) {
                    sb.append(param);
                }
                if (param instanceof StampFieldFun) {
                    this.buildSelectFieldFun(wrapper, stampTables,
                            (StampFieldFun) param, sb);
                }
            }
            sb.append(")");
        }
    }

    protected String getColumnType(KeyColumnType columnType, int len, int scale) {
        if (columnType == KeyColumnType.INT) {
            return "INT";
        }
        if (columnType == KeyColumnType.VARCHAR) {
            return "VARCHAR(" + len + ")";
        }
        if (columnType == KeyColumnType.CHAR) {
            return "CHAR(" + len + ")";
        }
        if (columnType == KeyColumnType.BLOB) {
            return "BLOB";
        }
        if (columnType == KeyColumnType.TEXT) {
            return "TEXT";
        }
        if (columnType == KeyColumnType.TINYINT) {
            return "TINYINT";
        }
        if (columnType == KeyColumnType.SMALLINT) {
            return "SMALLINT";
        }
        if (columnType == KeyColumnType.MEDIUMINT) {
            return "MEDIUMINT";
        }
        if (columnType == KeyColumnType.BIT) {
            return "BIT";
        }
        if (columnType == KeyColumnType.BIGINT) {
            return "BIGINT";
        }
        if (columnType == KeyColumnType.FLOAT) {
            return "FLOAT";
        }
        if (columnType == KeyColumnType.DOUBLE) {
            return "DOUBLE";
        }
        if (columnType == KeyColumnType.DECIMAL) {
            return "DECIMAL(" + len + "," + scale + ")";
        }
        if (columnType == KeyColumnType.BOOLEAN) {
            return "BOOLEAN";
        }
        if (columnType == KeyColumnType.DATE) {
            return "DATE";
        }
        if (columnType == KeyColumnType.TIME) {
            return "TIME";
        }
        if (columnType == KeyColumnType.DATETIME) {
            return "DATETIME";
        }
        if (columnType == KeyColumnType.TIMESTAMP) {
            return "TIMESTAMP";
        }
        if (columnType == KeyColumnType.YEAR) {
            return "YEAR";
        }
        return null;
    }
}
