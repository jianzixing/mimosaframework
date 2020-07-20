package org.mimosaframework.orm.platform.postgresql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.PlatformStampShare;
import org.mimosaframework.orm.platform.SQLDataPlaceholder;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.List;

public class PostgreSQLStampShare extends PlatformStampShare {
    public void buildWhere(MappingGlobalWrapper wrapper,
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
                    key = this.commonality.getReference().getColumnName(wrapper, stampTables, leftColumn);
                    sb.append(key);
                } else if (leftFun != null) {
                    this.buildSelectFieldFun(wrapper, stampTables, leftFun, sb);
                    key = leftFun.funName;
                } else if (leftValue != null) {
                    sb.append(leftValue);
                }

                if (where.not) sb.append(" NOT");
                sb.append(" " + where.operator + " ");

                if (rightColumn != null) {
                    String columnName = this.commonality.getReference().getColumnName(wrapper, stampTables, rightColumn);
                    sb.append(columnName);
                } else if (rightFun != null) {
                    this.buildSelectFieldFun(wrapper, stampTables, rightFun, sb);
                } else if (rightValue != null) {
                    this.parseValue(sb, key, rightValue, placeholders);
                }
            }
            if (whereType == KeyWhereType.KEY_AND) {
                if (leftColumn != null) {
                    key = this.commonality.getReference().getColumnName(wrapper, stampTables, leftColumn);
                    sb.append(key);
                } else if (leftFun != null) {
                    this.buildSelectFieldFun(wrapper, stampTables, leftFun, sb);
                    key = leftFun.funName;
                } else if (leftValue != null) {
                    sb.append(leftValue);
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
                    placeholder2.setName(key + "&End");
                }
                placeholder2.setValue(rightValueEnd);
                placeholders.add(placeholder2);
            }

            if (whereType == KeyWhereType.FUN) {
                if (where.fun != null
                        && where.fun.funName.equalsIgnoreCase("ISNULL")
                        && where.fun.params != null
                        && where.fun.params.length > 0
                        && where.fun.params[0] instanceof StampColumn) {
                    for (Object param : where.fun.params) {
                        if (param instanceof StampColumn) {
                            sb.append(this.commonality.getReference().getColumnName(wrapper, stampTables, ((StampColumn) param)));
                            break;
                        }
                    }

                    if (where.not) {
                        sb.append(" IS NOT NULL");
                    } else {
                        sb.append(" IS NULL");
                    }
                } else {
                    if (where.not) sb.append("NOT ");
                    this.buildSelectFieldFun(wrapper, stampTables, fun, sb);
                }
            }
        }

        if (next != null) {
            if (where.whereType != null) {
                if (where.nextLogic == KeyLogic.AND) {
                    sb.append(" AND ");
                } else if (where.nextLogic == KeyLogic.OR) {
                    sb.append(" OR ");
                } else {
                    sb.append(" AND ");
                }
            }

            this.buildWhere(wrapper, placeholders, stampTables, next, sb);
        }
    }

    public void buildSelectFieldFun(MappingGlobalWrapper wrapper,
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
                    sb.append(this.commonality.getReference().getColumnName(wrapper, stampTables, (StampColumn) param));
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

    public void addCommentSQL(MappingGlobalWrapper wrapper,
                              StampAction action,
                              Object param,
                              String commentStr,
                              int type) {
        Class table = null;
        String tableStr = null;
        if (action instanceof StampAlter) {
            table = ((StampAlter) action).tableClass;
            tableStr = ((StampAlter) action).tableName;
        }
        if (action instanceof StampCreate) {
            table = ((StampCreate) action).tableClass;
            tableStr = ((StampCreate) action).tableName;
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
            comment.append(this.commonality.getReference().getColumnName(wrapper, action, column));
        }
        if (type == 2) {
            String tableName = this.commonality.getReference().getTableName(wrapper, table, tableStr);
            comment.append("COMMENT ON TABLE " + tableName);
        }
        comment.append(" IS ");
        comment.append("'" + commentStr + "'");
        this.commonality.getSection().getBuilders().add(new ExecuteImmediate(comment));
    }
}
