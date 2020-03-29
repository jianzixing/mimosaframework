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

    protected String getColumnName(MappingGlobalWrapper wrapper, StampTables stampTables, StampColumn column) {
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

            List<StampTables.STItem> tables = stampTables.getTables();
            if (tables != null) {
                if (StringTools.isNotEmpty(tableAliasName)) {
                    for (StampTables.STItem stItem : tables) {
                        if (tableAliasName.equals(stItem.tableAliasName)) {
                            MappingTable mappingTable = wrapper.getMappingTable(stItem.table);
                            if (mappingTable != null) {
                                MappingField mappingField = mappingTable.getMappingFieldByName(columnName);
                                if (mappingField != null) {
                                    return RS + tableAliasName + RE + "." + RS + mappingField.getMappingColumnName() + RE;
                                }
                            }
                        }
                    }
                }

                for (StampTables.STItem stItem : tables) {
                    MappingTable mappingTable = wrapper.getMappingTable(stItem.table);
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
                              StampTables stampTables,
                              StampWhere where,
                              StringBuilder sb) {
        StampSelectFieldFun compareFun = null;
        if (where instanceof StampHaving) {
            compareFun = ((StampHaving) where).compareFun;
        }
        StampColumn column = where.column;
        StampWhere next = where.next;
        StampWhere wrapWhere = where.wrapWhere;
        if (column != null) {
            String key = null;
            if (compareFun != null) {
                this.buildSelectFieldFun(wrapper, stampTables, compareFun, sb);
                key = "HAVING&" + compareFun.funName;
            } else {
                key = this.getColumnName(wrapper, stampTables, column);
                sb.append(key);
            }
            sb.append(" " + where.operator + " ");
            if (where.compareColumn != null) {
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
        }
    }

    protected void buildSelectFieldFun(MappingGlobalWrapper wrapper,
                                       StampTables stampTables,
                                       StampSelectFieldFun fun,
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
                if (param instanceof StampSelectFieldFun) {
                    this.buildSelectFieldFun(wrapper, stampTables,
                            (StampSelectFieldFun) param, sb);
                }
            }
            sb.append(")");
        }
    }
}
