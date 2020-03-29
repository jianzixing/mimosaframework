package org.mimosaframework.orm.platform.mysql;

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

    protected void buildWhere(MappingGlobalWrapper wrapper,
                              List<SQLDataPlaceholder> placeholders,
                              StampTables stampTables,
                              StampWhere where,
                              StringBuilder sb) {
        StampColumn column = where.column;
        StampWhere next = where.next;
        StampWhere wrapWhere = where.wrapWhere;
        if (column != null) {
            String key = this.getColumnName(wrapper, stampTables, column);
            sb.append(key);
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
}
