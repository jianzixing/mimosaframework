package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.platform.SQLDataPlaceholder;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class MysqlStampDelete extends MysqlAbstractStamp implements StampDeleteBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampDelete delete) {
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ");
        String[] aliasNames = delete.aliasNames;
        Class[] tables = delete.tables;

        if (tables != null && tables.length > 0) {
            int i = 0;
            for (Class table : tables) {
                sb.append(this.getTableName(wrapper, table, null));
                i++;
                if (i == tables.length) {
                    sb.append(",");
                }
            }

            if (aliasNames != null && aliasNames.length > 0) {
                sb.append(",");
            }
        }

        if (aliasNames != null && aliasNames.length > 0) {
            int i = 0;
            for (String aliasName : aliasNames) {
                sb.append(aliasName);
                i++;
                if (i == tables.length) {
                    sb.append(",");
                }
            }
        }

        sb.append(" FROM ");

        StampFrom[] froms = delete.froms;
        int i = 0;
        for (StampFrom from : froms) {
            sb.append(this.getTableName(wrapper, from.table, from.name));
            sb.append(" AS " + from.aliasName);
            i++;
            if (i != froms.length) sb.append(",");
        }

        if (delete.where != null) {
            sb.append(" WHERE ");
            this.buildWhere(wrapper, placeholders, delete, delete.where, sb);
        }
        if (delete.orderBy != null) {
            StampOrderBy[] orderBy = delete.orderBy;
            sb.append(" ORDER BY ");
            int j = 0;
            for (StampOrderBy ob : orderBy) {
                sb.append(this.getColumnName(wrapper, delete, ob.column));
                if (ob.sortType == KeySortType.ASC)
                    sb.append(" ASC");
                else
                    sb.append(" DESC");
                j++;
                if (j != orderBy.length) sb.append(",");
            }
        }

        if (delete.limit != null) {
            sb.append(" LIMIT " + delete.limit.start + "," + delete.limit.limit);
        }

        return null;
    }

    private void buildWhere(MappingGlobalWrapper wrapper,
                            List<SQLDataPlaceholder> placeholders,
                            StampDelete delete,
                            StampWhere where,
                            StringBuilder sb) {
        StampColumn column = where.column;
        StampWhere next = where.next;
        StampWhere wrapWhere = where.wrapWhere;
        if (column != null) {
            String key = this.getColumnName(wrapper, delete, column);
            sb.append(key);
            sb.append(" " + where.operator + " ");
            if (where.compareColumn != null) {
                sb.append(this.getColumnName(wrapper, delete, where.compareColumn));
            } else if (where.value != null) {
                sb.append("?");
                SQLDataPlaceholder placeholder = new SQLDataPlaceholder();
                placeholder.setName(key);
                placeholder.setValue(where.value);
                placeholders.add(placeholder);
            }

        } else if (wrapWhere != null) {
            sb.append("(");
            this.buildWhere(wrapper, placeholders, delete, wrapWhere, sb);
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
