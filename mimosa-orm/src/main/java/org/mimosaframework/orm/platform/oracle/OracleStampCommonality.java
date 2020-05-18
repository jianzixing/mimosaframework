package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.PlatformDialect;
import org.mimosaframework.orm.platform.PlatformStampCommonality;
import org.mimosaframework.orm.platform.SQLDataPlaceholder;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class OracleStampCommonality extends PlatformStampCommonality {
    protected static final String RS = "\"";
    protected static final String RE = "\"";
    protected static OraclePlatformDialect dialect = new OraclePlatformDialect();

    @Override
    protected PlatformDialect getDialect() {
        return dialect;
    }

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
                    sb.append(leftValue);
                }

                if (where.not) sb.append(" NOT");
                sb.append(" " + where.operator + " ");

                if (rightColumn != null) {
                    String columnName = this.getColumnName(wrapper, stampTables, rightColumn);
                    sb.append(columnName);
                } else if (rightFun != null) {
                    this.buildSelectFieldFun(wrapper, stampTables, rightFun, sb);
                } else if (rightValue != null) {
                    this.parseValue(sb, key, rightValue, placeholders);
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
                    placeholder1.setName(key + "&End");
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
                            sb.append(this.getColumnName(wrapper, stampTables, ((StampColumn) param)));
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

    /**
     * ORACLE的数据类型 -- ORACLE的数据类型
     * 常用的数据库字段类型如下：
     * 字段类型 中文说明 限制条件 其它说明
     * CHAR 固定长度字符串 最大长度2000 bytes
     * VARCHAR2 可变长度的字符串 最大长度4000 bytes 可做索引的最大长度749
     * NCHAR 根据字符集而定的固定长度字符串 最大长度2000 bytes
     * NVARCHAR2 根据字符集而定的可变长度字符串 最大长度4000 bytes
     * DATE 日期（日-月-年） DD-MM-YY（HH-MI-SS） 无千虫问题
     * LONG 超长字符串 最大长度2G（231-1） 足够存储大部头著作
     * RAW 固定长度的二进制数据 最大长度2000 bytes 可存放多媒体图象声音等
     * LONG RAW 可变长度的二进制数据 最大长度2G 同上
     * BLOB 二进制数据 最大长度4G
     * CLOB 字符数据 最大长度4G
     * NCLOB 根据字符集而定的字符数据 最大长度4G
     * BFILE 存放在数据库外的二进制数据 最大长度4G
     * ROWID 数据表中记录的唯一行号 10 bytes ********.****.****格式，*为0或1
     * NROWID 二进制数据表中记录的唯一行号 最大长度4000 bytes
     * NUMBER(P,S) 数字类型 P为总位数，S为小数位数
     * DECIMAL(P,S) 数字类型 P为总位数，S为小数位数
     * INTEGER 整数类型 小的整数
     * FLOAT 浮点数类型 NUMBER(38)，双精度
     * REAL 实数类型 NUMBER(63)，精度更高
     */

    protected void addCommentSQL(MappingGlobalWrapper wrapper,
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

    protected void addAutoIncrement(MappingGlobalWrapper wrapper, Class table, String tableStr) {
        String tableName = this.getTableName(wrapper, table, tableStr);
        String seqName = tableName + "_SEQ";
        this.getDeclares().add("SEQUENCE_COUNT NUMBER");
        this.getBuilders().add(new ExecuteImmediate().setProcedure("SELECT COUNT(1) INTO SEQUENCE_COUNT FROM USER_SEQUENCES WHERE SEQUENCE_NAME = '" + seqName + "'"));
        this.getBuilders().add(new ExecuteImmediate("IF SEQUENCE_COUNT = 0 THEN ",
                "CREATE SEQUENCE " + seqName + " INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 9999999999999999", "END IF"));
    }

    /**
     * 无order by
     * SELECT *
     * FROM (SELECT ROWNUM AS rowno, t.*
     * FROM emp t
     * WHERE hire_date BETWEEN TO_DATE ('20060501', 'yyyymmdd')
     * AND TO_DATE ('20060731', 'yyyymmdd')
     * AND ROWNUM <= 20) table_alias
     * WHERE table_alias.rowno >= 10;
     * <p>
     * <p>
     * 有order by
     * SELECT *
     * FROM (SELECT tt.*, ROWNUM AS rowno
     * FROM (  SELECT t.*
     * FROM emp t
     * WHERE hire_date BETWEEN TO_DATE ('20060501', 'yyyymmdd')
     * AND TO_DATE ('20060731', 'yyyymmdd')
     * ORDER BY create_time DESC, emp_no) tt
     * WHERE ROWNUM <= 20) table_alias
     * WHERE table_alias.rowno >= 10;
     *
     * @param limit
     */
    protected void limit(StampLimit limit) {
    }

    protected StringBuilder multiDeleteOrUpdate(MappingGlobalWrapper wrapper,
                                                Class delTable,
                                                String delTableName,
                                                StampFrom[] froms,
                                                StampWhere where,
                                                StampOrderBy[] orderBys,
                                                StampLimit limit,
                                                List<SQLDataPlaceholder> placeholders,
                                                StampAction action,
                                                Map<String, String> selectFieldMap) {
        StringBuilder select = new StringBuilder();
        select.append("SELECT ");
        if (selectFieldMap != null && selectFieldMap.size() > 0) {
            Iterator<Map.Entry<String, String>> iterator = selectFieldMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                select.append(entry.getKey());
                select.append(" AS " + entry.getValue());
                if (iterator.hasNext()) select.append(", ");
            }
        } else {
            String selectFields = "*";
            boolean isIn = false;
            for (StampFrom from : froms) {
                if (from.aliasName != null
                        && StringTools.isNotEmpty(delTableName)
                        && from.aliasName.equalsIgnoreCase(delTableName)) {
                    selectFields = from.aliasName.toUpperCase() + ".*";
                    isIn = true;
                }
            }
            if (!isIn && (delTable != null || StringTools.isNotEmpty(delTableName))) {
                selectFields = this.getTableName(wrapper, delTable, delTableName) + ".*";
            }
            select.append(selectFields);
        }
        select.append(" FROM");
        int i = 0;
        for (StampFrom from : froms) {
            select.append(" " + this.getTableName(wrapper, from.table, from.name));
            if (StringTools.isNotEmpty(from.aliasName)) {
                select.append(" " + from.aliasName.toUpperCase());
            }
            i++;
            if (i != froms.length) select.append(",");
        }
        if (where != null) {
            select.append(" WHERE ");
            this.buildWhere(wrapper, placeholders, action, where, select);
        }

        if (orderBys != null && orderBys.length > 0) {
            select.append(" ORDER BY ");
            int j = 0;
            for (StampOrderBy ob : orderBys) {
                select.append(this.getColumnName(wrapper, action, ob.column));
                if (ob.sortType == KeySortType.ASC)
                    select.append(" ASC");
                else
                    select.append(" DESC");
                j++;
                if (j != orderBys.length) select.append(",");
            }
        }

        if (limit != null) {
            long start = limit.start, len = start + limit.limit;
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM (SELECT RN_TABLE_ALIAS.*, ROWNUM AS RN_ALIAS FROM (");
            sb.append(select);
            sb.append(") RN_TABLE_ALIAS WHERE ROWNUM <= " + len + ") RN_TABLE_ALIAS_2 WHERE RN_TABLE_ALIAS_2.RN_ALIAS >= " + start);
            return sb;
        } else {
            return select;
        }
    }
}
