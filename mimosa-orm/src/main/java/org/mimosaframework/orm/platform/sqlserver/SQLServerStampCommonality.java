package org.mimosaframework.orm.platform.sqlserver;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.PlatformStampCommonality;
import org.mimosaframework.orm.platform.SQLDataPlaceholder;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.List;

public abstract class SQLServerStampCommonality extends PlatformStampCommonality {
    protected static final String RS = "[";
    protected static final String RE = "]";

    protected boolean isDeclareCheckComment = false;
    protected boolean isDeclareCheckTableComment = false;

    public SQLServerStampCommonality() {
        this.declareInBegin = true;
    }

    protected void appendBuilderDeclare(StringBuilder nsb, boolean isIn) {
        for (String s : declares) {
            nsb.append(NL_TAB + "DECLARE " + s + ";");
        }
    }

    protected void appendBuilderWrapper(ExecuteImmediate item, StringBuilder nsb) {
        if (StringTools.isNotEmpty(item.preview)) {
            nsb.append(NL_TAB + item.preview + " ");
        } else {
            nsb.append(NL_TAB);
        }

        if (StringTools.isNotEmpty(item.end)) {
            nsb.append(item.sql + ";");
            nsb.append(item.end + ";");
        } else {
            nsb.append(item.sql + "; ");
        }
    }

    protected String getTableName(MappingGlobalWrapper wrapper,
                                  Class table,
                                  String tableName) {
        return this.getTableName(wrapper, table, tableName, true);
    }

    protected String getTableName(MappingGlobalWrapper wrapper,
                                  Class table,
                                  String tableName, boolean hasRes) {
        String RS = this.RS;
        String RE = this.RE;
        if (!hasRes) {
            RS = "";
            RE = "";
        }
        if (table != null) {
            MappingTable mappingTable = wrapper.getMappingTable(table);
            if (mappingTable != null) {
                return RS + mappingTable.getMappingTableName() + RE;
            }
        } else {
            return RS + tableName + RE;
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
                    return tableAliasName + "." + columnName;
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
                                return tableAliasName + "." + RS + mappingField.getMappingColumnName() + RE;
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
                        return mappingTable.getMappingTableName()
                                + "."
                                + RS + mappingField.getMappingColumnName() + RE;
                    } else {
                        return mappingTable.getMappingTableName()
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
                return tableAliasName + "." + RS + columnName + RE;
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
                                 int type,
                                 boolean isCheckHasTable) {
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
        String tableName = this.getTableName(wrapper, table, tableStr, false);

        if (type == 1) {
            StampColumn column = (StampColumn) param;
            if (table != null) {
                column.table = table;
            } else if (StringTools.isNotEmpty(tableStr)) {
                column.tableAliasName = tableStr;
            }
            String columnName = this.getColumnName(wrapper, action, new StampColumn(column.column), false);
            if (!isDeclareCheckComment) {
                this.getDeclares().add("@EXIST_COLUMN_COMMENT INT");
                this.isDeclareCheckComment = true;
            }
            this.getBuilders().add(new ExecuteImmediate()
                    .setProcedure("SELECT @EXIST_COLUMN_COMMENT=(SELECT COUNT(1) FROM SYS.COLUMNS A " +
                            "LEFT JOIN SYS.EXTENDED_PROPERTIES G ON (A.OBJECT_ID = G.MAJOR_ID AND G.MINOR_ID = A.COLUMN_ID) " +
                            "WHERE OBJECT_ID = (SELECT OBJECT_ID FROM SYS.TABLES WHERE NAME = '" + tableName + "') " +
                            "AND A.NAME='" + columnName + "' AND G.VALUE IS NOT NULL)"));
            this.getBuilders().add(new ExecuteImmediate().setProcedure(
                    (isCheckHasTable ? "IF (@HAS_TABLE = 0) BEGIN " + NL_TAB : "") +
                            "IF (@EXIST_COLUMN_COMMENT = 1) " +
                            "EXEC SP_UPDATEEXTENDEDPROPERTY 'MS_Description', '" + commentStr + "', 'SCHEMA', 'dbo', 'TABLE', '" + tableName + "', 'COLUMN', '" + columnName + "';" +
                            NL_TAB + "ELSE " +
                            "EXEC SP_ADDEXTENDEDPROPERTY 'MS_Description', '" + commentStr + "', 'SCHEMA', 'dbo', 'TABLE', '" + tableName + "', 'COLUMN', '" + columnName + "';" +
                            (isCheckHasTable ? NL_TAB + "END" : "")
            ));
        }
        if (type == 2) {
            if (!isDeclareCheckTableComment) {
                this.getDeclares().add("@EXIST_TABLE_COMMENT INT");
                this.isDeclareCheckTableComment = true;
            }
            this.getBuilders().add(new ExecuteImmediate()
                    .setProcedure("SELECT @EXIST_TABLE_COMMENT=(SELECT COUNT(DISTINCT B.NAME) " +
                            "FROM SYS.SYSCOLUMNS A " +
                            "INNER JOIN SYS.SYSOBJECTS B ON A.ID = B.ID " +
                            "LEFT JOIN SYS.SYSCOMMENTS C ON A.CDEFAULT = C.ID " +
                            "LEFT JOIN SYS.EXTENDED_PROPERTIES F ON B.ID = F.MAJOR_ID AND F.MINOR_ID = 0 " +
                            "WHERE B.NAME='t_tt' AND F.VALUE IS NOT NULL)"));
            this.getBuilders().add(new ExecuteImmediate().setProcedure(
                    (isCheckHasTable ? "IF (@HAS_TABLE = 0) BEGIN " + NL_TAB : "") +
                            "IF (@EXIST_TABLE_COMMENT = 1) " +
                            "EXEC SP_UPDATEEXTENDEDPROPERTY 'MS_Description', '" + commentStr + "', 'SCHEMA', 'dbo', 'TABLE', '" + tableName + "'" +
                            NL_TAB + "ELSE " +
                            "EXEC SP_ADDEXTENDEDPROPERTY 'MS_Description', '" + commentStr + "', 'SCHEMA', 'dbo', 'TABLE', '" + tableName + "'" +
                            (isCheckHasTable ? NL_TAB + "END" : "")
            ));
        }
    }
}
