package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.Iterator;
import java.util.List;

public class PlatformStampShare {
    protected PlatformStampCommonality commonality;

    public void setCommonality(PlatformStampCommonality commonality) {
        this.commonality = commonality;
    }

    public void addCommentSQL(MappingGlobalWrapper wrapper,
                              StampAction action,
                              Object param,
                              String commentStr,
                              int type) {
    }

    public void addCommentSQL(MappingGlobalWrapper wrapper,
                              StampAction action,
                              Object param,
                              String commentStr,
                              int type,
                              boolean isCheckHasTable) {

    }

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

    public void parseValue(StringBuilder sb,
                           String keyName,
                           Object value,
                           List<SQLDataPlaceholder> placeholders) {
        if (value.getClass().isArray()) {
            Object[] values = (Object[]) value;
            sb.append("(");
            for (int i = 0; i < values.length; ) {
                sb.append("?");

                SQLDataPlaceholder placeholder = new SQLDataPlaceholder();
                placeholder.setName(keyName + "&" + i);
                placeholder.setValue(values[i]);
                placeholders.add(placeholder);

                i++;
                if (i != values.length) sb.append(",");
            }
            sb.append(")");
        } else if (value instanceof Iterable) {
            Iterable iterable = (Iterable) value;
            Iterator iteratorValue = iterable.iterator();
            int i = 0;
            sb.append("(");
            while (iteratorValue.hasNext()) {
                sb.append("?");

                SQLDataPlaceholder placeholder = new SQLDataPlaceholder();
                placeholder.setName(keyName + "&" + i);
                placeholder.setValue(iteratorValue.next());
                placeholders.add(placeholder);

                i++;
                if (iteratorValue.hasNext()) sb.append(",");
            }
            sb.append(")");
        } else {
            sb.append("?");

            SQLDataPlaceholder placeholder = new SQLDataPlaceholder();
            placeholder.setName(keyName);
            placeholder.setValue(value);
            placeholders.add(placeholder);
        }
    }

    public String getColumnType(KeyColumnType columnType, int len, int scale) {
        return null;
    }


    // oracle
    public void addAutoIncrement(MappingGlobalWrapper wrapper, Class table, String tableStr) {

    }
}
