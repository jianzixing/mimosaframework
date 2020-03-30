package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.platform.SQLDataPlaceholder;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class MysqlStampSelect extends MysqlAbstractStamp implements StampCombineBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampSelect select = (StampSelect) action;
        StringBuilder sb = new StringBuilder();
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();

        sb.append("SELECT ");
        StampSelectField[] columns = select.columns;
        int i = 0;
        for (StampSelectField column : columns) {
            this.buildSelectField(wrapper, select, column, sb, placeholders);
            i++;
            if (i != columns.length) sb.append(",");
        }
        return null;
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

        if (column != null) {
            if (StringTools.isNotEmpty(tableAliasName)) {
                sb.append(RS + tableAliasName + RE + ".");
            }
            sb.append(this.getColumnName(wrapper, select, column));
            if (StringTools.isNotEmpty(aliasName)) {
                sb.append(" AS " + RS + aliasName + RE);
            }
        } else if (fun != null) {
            this.buildSelectFieldFun(wrapper, select, fun, sb);

            if (StringTools.isNotEmpty(aliasName)) {
                sb.append(" AS " + RS + aliasName + RE);
            }
        }

        sb.append(" FROM ");

        StampFrom[] froms = select.froms;
        int i = 0;
        for (StampFrom from : froms) {
            sb.append(this.getTableName(wrapper, from.table, from.name));
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
                sb.append(" " + this.getTableName(wrapper, join.table, join.name));
                if (StringTools.isNotEmpty(join.tableAliasName)) {
                    sb.append(" AS " + RS + join.tableAliasName + RE);
                }
                sb.append(" ");
                this.buildWhere(wrapper, placeholders, select, join.on, sb);
            }
        }

        sb.append(" ");
        if (select.where != null) {
            this.buildWhere(wrapper, placeholders, select, select.where, sb);
        }
        sb.append(" ");

        if (select.groupBy != null) {
            sb.append("GROUP BY ");
            StampColumn[] groupBy = select.groupBy;
            if (groupBy != null) {
                int j = 0;
                for (StampColumn gb : groupBy) {
                    sb.append(this.getColumnName(wrapper, select, gb));
                    j++;
                    if (j != groupBy.length) sb.append(",");
                }
            }
            sb.append(" ");
        }

        if (select.having != null) {
            sb.append("HAVING ");
            this.buildWhere(wrapper, placeholders, select, select.where, sb);
            sb.append(" ");
        }

        if (select.orderBy != null) {
            StampOrderBy[] orderBy = select.orderBy;
            sb.append("ORDER BY ");
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
            sb.append(" ");
        }

        if (select.limit != null) {
            sb.append("LIMIT " + select.limit.start + "," + select.limit.limit);
        }
    }
}
