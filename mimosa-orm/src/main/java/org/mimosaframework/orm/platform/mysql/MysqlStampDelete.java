package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.platform.SQLDataPlaceholder;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class MysqlStampDelete extends MysqlStampCommonality implements StampCombineBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampDelete delete = (StampDelete) action;
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ");

        String aliasName = delete.delTableAlias;
        Class table = delete.delTable;

        if (table != null) {
            sb.append(this.getTableName(wrapper, table, null));
        } else if (StringTools.isNotEmpty(aliasName)) {
            sb.append(aliasName);
        }

        if (StringTools.isNotEmpty(aliasName) || table != null) sb.append(" ");
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
