package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.PlatformStampCommonality;
import org.mimosaframework.orm.platform.SQLDataPlaceholder;
import org.mimosaframework.orm.platform.oracle.OracleStampCommonality;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public abstract class DB2StampCommonality extends PlatformStampCommonality {
    protected static final String RS = "\"";
    protected static final String RE = "\"";

    protected String getTableName(MappingGlobalWrapper wrapper,
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

    protected String getColumnName(MappingGlobalWrapper wrapper, StampAction stampTables, StampColumn column) {
        return this.getColumnName(wrapper, stampTables, column, true);
    }

    protected String getColumnName(MappingGlobalWrapper wrapper,
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

    protected void buildWhere(MappingGlobalWrapper wrapper,
                              List<SQLDataPlaceholder> placeholders,
                              StampAction stampTables,
                              StampWhere where,
                              StringBuilder sb) {
        KeyWhereType whereType = where.whereType;
        StampWhere next = where.next;

        if (whereType == KeyWhereType.WRAP) {
            StampWhere wrapWhere = where.wrapWhere;
            sb.append("(");
            this.buildWhere(wrapper, placeholders, stampTables, wrapWhere, sb);
            sb.append(")");
        } else {
            StampFieldFun fun = where.fun;
            StampColumn leftColumn = where.leftColumn;
            StampFieldFun leftFun = where.leftFun;
            Object leftValue = where.leftValue;
            StampColumn rightColumn = where.rightColumn;
            StampFieldFun rightFun = where.rightFun;
            Object rightValue = where.rightValue;
            Object rightValueEnd = where.rightValueEnd;

            String key = null;
            if (whereType == KeyWhereType.NORMAL) {
                if (leftColumn != null) {
                    key = this.getColumnName(wrapper, stampTables, leftColumn);
                    sb.append(key);
                } else if (leftFun != null) {
                    this.buildSelectFieldFun(wrapper, stampTables, leftFun, sb);
                    key = leftFun.funName;
                } else if (leftValue != null) {
                    sb.append("?");

                    SQLDataPlaceholder placeholder = new SQLDataPlaceholder();
                    placeholder.setName("Unknown");
                    placeholder.setValue(leftValue);
                    placeholders.add(placeholder);
                }

                if (where.not) sb.append(" NOT");
                sb.append(" " + where.operator + " ");

                if (rightColumn != null) {
                    String columnName = this.getColumnName(wrapper, stampTables, rightColumn);
                    sb.append(columnName);
                } else if (rightFun != null) {
                    this.buildSelectFieldFun(wrapper, stampTables, rightFun, sb);
                } else if (rightValue != null) {
                    sb.append("?");

                    SQLDataPlaceholder placeholder = new SQLDataPlaceholder();
                    placeholder.setName(key);
                    placeholder.setValue(rightValue);
                    placeholders.add(placeholder);
                }
            }
            if (whereType == KeyWhereType.KEY_AND) {
                if (leftColumn != null) {
                    key = this.getColumnName(wrapper, stampTables, leftColumn);
                    sb.append(key);
                } else if (leftFun != null) {
                    this.buildSelectFieldFun(wrapper, stampTables, leftFun, sb);
                    key = leftFun.funName;
                } else if (leftValue != null) {
                    sb.append("?");

                    SQLDataPlaceholder placeholder = new SQLDataPlaceholder();
                    placeholder.setName("Unknown");
                    placeholder.setValue(leftValue);
                    placeholders.add(placeholder);
                }
                if (where.not) sb.append(" NOT");
                sb.append(" " + where.operator + " ");

                sb.append("?");

                SQLDataPlaceholder placeholder1 = new SQLDataPlaceholder();
                if (StringTools.isEmpty(key)) {
                    placeholder1.setName("Unknown&Start");
                } else {
                    placeholder1.setName(key + "&Start");
                }
                placeholder1.setValue(rightValue);
                placeholders.add(placeholder1);

                sb.append(" AND ");

                sb.append("?");

                SQLDataPlaceholder placeholder2 = new SQLDataPlaceholder();
                if (StringTools.isEmpty(key)) {
                    placeholder2.setName("Unknown&End");
                } else {
                    placeholder1.setName(key + "&End");
                }
                placeholder2.setValue(rightValueEnd);
                placeholders.add(placeholder2);
            }

            if (whereType == KeyWhereType.FUN) {
                if (where.not) sb.append("NOT ");
                this.buildSelectFieldFun(wrapper, stampTables, fun, sb);
            }
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
        String funName = fun.funName.toUpperCase();
        Object[] params = fun.params;

        sb.append(funName);
        if (params != null) {
            sb.append("(");
            for (Object param : params) {
                if (param instanceof StampColumn) {
                    sb.append(this.getColumnName(wrapper, stampTables, (StampColumn) param));
                }
                if (param instanceof StampKeyword) {
                    if (((StampKeyword) param).distinct) sb.append("DISTINCT ");
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

    protected void addCommentSQL(MappingGlobalWrapper wrapper,
                                 StampAction action,
                                 Object param,
                                 String commentStr,
                                 int type) {
        Class table = null;
        String tableStr = null;
        if (action instanceof StampAlter) {
            table = ((StampAlter) action).table;
            tableStr = ((StampAlter) action).name;
        }
        if (action instanceof StampCreate) {
            table = ((StampCreate) action).table;
            tableStr = ((StampCreate) action).name;
        }

        StringBuilder comment = new StringBuilder();
        if (type == 1) {
            StampColumn column = (StampColumn) param;
            comment.append("COMMENT ON COLUMN ");
            if (table != null) {
                column.table = table;
            } else if (StringTools.isNotEmpty(tableStr)) {
                column.tableAliasName = tableStr;
            }
            comment.append(this.getColumnName(wrapper, action, column));
        }
        if (type == 2) {
            String tableName = this.getTableName(wrapper, table, tableStr);
            comment.append("COMMENT ON TABLE " + tableName);
        }
        comment.append(" IS ");
        comment.append("''" + commentStr + "''");
        this.getBuilders().add(new ExecuteImmediate(comment));
    }
}
