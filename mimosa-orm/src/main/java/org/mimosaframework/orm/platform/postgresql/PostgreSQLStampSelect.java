package org.mimosaframework.orm.platform.postgresql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.platform.SQLDataPlaceholder;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class PostgreSQLStampSelect extends PostgreSQLStampCommonality implements StampCombineBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampSelect select = (StampSelect) action;
        StringBuilder sb = new StringBuilder();
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();

        this.buildSelect(wrapper, select, sb, placeholders);
        return new SQLBuilderCombine(sb.toString(), placeholders);
    }

    private void buildSelect(MappingGlobalWrapper wrapper,
                             StampSelect select,
                             StringBuilder sb,
                             List<SQLDataPlaceholder> placeholders) {
        sb.append("SELECT ");
        StampSelectField[] columns = select.columns;
        int m = 0;
        for (StampSelectField column : columns) {
            this.buildSelectField(wrapper, select, column, sb, placeholders);
            m++;
            if (m != columns.length) sb.append(",");
        }

        sb.append(" FROM ");

        StampFrom[] froms = select.froms;
        int i = 0;
        for (StampFrom from : froms) {
            this.buildFrom(wrapper, sb, select, placeholders, from, null);
            if (StringTools.isNotEmpty(from.aliasName)) {
                sb.append(" AS " + RS + from.aliasName + RE);
            }
            i++;
            if (i != froms.length) sb.append(",");
        }

        StampSelectJoin[] joins = select.joins;
        if (joins != null) {
            for (StampSelectJoin join : joins) {
                if (join.joinType == KeyJoinType.LEFT) {
                    sb.append(" LEFT JOIN");
                }
                if (join.joinType == KeyJoinType.INNER) {
                    sb.append(" INNER JOIN");
                }
                this.buildFrom(wrapper, sb, select, placeholders, null, join);
                if (StringTools.isNotEmpty(join.tableAliasName)) {
                    sb.append(" AS " + RS + join.tableAliasName + RE);
                }
                if (join.on != null) {
                    sb.append(" ON ");
                    this.buildWhere(wrapper, placeholders, select, join.on, sb);
                }
            }
        }

        if (select.where != null) {
            sb.append(" WHERE ");
            this.buildWhere(wrapper, placeholders, select, select.where, sb);
        }

        if (select.groupBy != null && select.groupBy.length > 0) {
            sb.append(" GROUP BY ");
            StampColumn[] groupBy = select.groupBy;
            if (groupBy != null) {
                int j = 0;
                for (StampColumn gb : groupBy) {
                    sb.append(this.getColumnName(wrapper, select, gb));
                    j++;
                    if (j != groupBy.length) sb.append(",");
                }
            }
        }

        if (select.having != null) {
            sb.append(" HAVING ");
            this.buildWhere(wrapper, placeholders, select, select.having, sb);
        }

        if (select.orderBy != null && select.orderBy.length > 0) {
            StampOrderBy[] orderBy = select.orderBy;
            sb.append(" ORDER BY ");
            int j = 0;
            for (StampOrderBy ob : orderBy) {
                sb.append(this.getColumnName(wrapper, select, ob.column));
                if (ob.sortType == KeySortType.ASC)
                    sb.append(" ASC");
                else
                    sb.append(" DESC");
                j++;
                if (j != orderBy.length) sb.append(",");
            }
        }

        if (select.limit != null) {
            sb.append(" LIMIT " + select.limit.limit + " OFFSET " + select.limit.start);
        }
    }

    private void buildSelectField(MappingGlobalWrapper wrapper,
                                  StampSelect select,
                                  StampSelectField field,
                                  StringBuilder sb,
                                  List<SQLDataPlaceholder> placeholders) {
        StampColumn column = field.column;
        StampFieldFun fun = field.fun;
        String aliasName = field.aliasName;
        String tableAliasName = field.tableAliasName;

        if (field.fieldType == KeyFieldType.ALL) {
            if (StringTools.isNotEmpty(tableAliasName)) {
                sb.append(RS + tableAliasName + RE + ".");
            }
            sb.append("*");
        } else if (field.fieldType == KeyFieldType.COLUMN) {
            if (StringTools.isNotEmpty(tableAliasName)) {
                sb.append(RS + tableAliasName + RE + ".");
            }
            sb.append(this.getColumnName(wrapper, select, column));
            if (StringTools.isNotEmpty(aliasName)) {
                sb.append(" AS " + RS + aliasName + RE);
            }
        } else if (field.fieldType == KeyFieldType.FUN) {
            this.buildSelectFieldFun(wrapper, select, fun, sb);

            if (StringTools.isNotEmpty(aliasName)) {
                sb.append(" AS " + RS + aliasName + RE);
            }
        }
    }

    private void buildFrom(MappingGlobalWrapper wrapper,
                           StringBuilder sb,
                           StampSelect select,
                           List<SQLDataPlaceholder> placeholders,

                           StampFrom from,
                           StampSelectJoin join) {
        if (from != null) {
            if (from.builder != null) {
                sb.append("(");
                this.buildSelect(wrapper, select, sb, placeholders);
                sb.append(")");
            } else {
                sb.append(this.getTableName(wrapper, from.table, from.name));
            }
        } else if (join != null) {
            if (join.builder != null) {
                sb.append(" (");
                this.buildSelect(wrapper, select, sb, placeholders);
                sb.append(")");
            } else {
                sb.append(" " + this.getTableName(wrapper, join.tableClass, join.tableName));
            }
        }
    }
}
