package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.platform.SQLDataPlaceholder;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class MysqlStampDelete extends MysqlAbstractStamp implements StampCombineBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampDelete delete = (StampDelete) action;
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ");
        String[] aliasNames = delete.delTableAlias;
        Class[] tables = delete.delTables;

        if (tables != null && tables.length > 0) {
            int i = 0;
            for (Class table : tables) {
                sb.append(this.getTableName(wrapper, table, null));
                i++;
                if (i != tables.length) {
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
                if (i != aliasNames.length) {
                    sb.append(",");
                }
            }
        }

        if ((aliasNames != null && aliasNames.length > 0) || (tables != null && tables.length > 0)) sb.append(" ");
        sb.append("FROM ");

        StampFrom[] froms = delete.froms;
        int i = 0;
        for (StampFrom from : froms) {
            sb.append(this.getTableName(wrapper, from.table, from.name));
            if (StringTools.isNotEmpty(from.aliasName)) {
                sb.append(" AS " + from.aliasName);
            }
            i++;
            if (i != froms.length) sb.append(",");
        }

        if (delete.where != null) {
            sb.append(" WHERE ");
            this.buildWhere(wrapper, placeholders, delete, delete.where, sb);
        }
        if (delete.orderBy != null && delete.orderBy.length > 0) {
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
            sb.append(" LIMIT " + delete.limit.limit);
        }

        return new SQLBuilderCombine(sb.toString(), placeholders);
    }
}
