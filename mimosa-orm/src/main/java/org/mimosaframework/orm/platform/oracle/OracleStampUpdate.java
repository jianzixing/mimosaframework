package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.platform.SQLDataPlaceholder;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class OracleStampUpdate extends OracleStampCommonality implements StampCombineBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampUpdate update = (StampUpdate) action;
        StringBuilder sb = new StringBuilder();
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();
        sb.append("UPDATE ");
        StampFrom fromTarget = update.table;
        if (fromTarget != null) {
            sb.append(this.getTableName(wrapper, fromTarget.table, fromTarget.name));
            if (StringTools.isNotEmpty(fromTarget.aliasName)) {
                sb.append(" AS " + RS + fromTarget.aliasName + RE);
            }
        }

        StampFrom[] froms = update.froms;
        if (froms != null && froms.length > 0) {
            sb.append(",");
            int i = 0;
            for (StampFrom table : froms) {
                sb.append(this.getTableName(wrapper, table.table, table.name));
                if (StringTools.isNotEmpty(table.aliasName)) {
                    sb.append(" AS " + RS + table.aliasName + RE);
                }
                i++;
                if (i != froms.length) sb.append(",");
            }
        }

        sb.append(" SET ");
        StampUpdateItem[] items = update.items;
        int k = 0;
        for (StampUpdateItem item : items) {
            this.buildUpdateItem(wrapper, update, item, sb, placeholders);
            k++;
            if (k != items.length) sb.append(",");
        }

        if (update.where != null) {
            sb.append(" WHERE ");
            this.buildWhere(wrapper, placeholders, update, update.where, sb);
        }

        if (update.orderBy != null && update.orderBy.length > 0) {
            StampOrderBy[] orderBy = update.orderBy;
            sb.append(" ORDER BY ");
            int j = 0;
            for (StampOrderBy ob : orderBy) {
                sb.append(this.getColumnName(wrapper, update, ob.column));
                if (ob.sortType == KeySortType.ASC)
                    sb.append(" ASC");
                else
                    sb.append(" DESC");
                j++;
                if (j != orderBy.length) sb.append(",");
            }
        }

        if (update.limit != null) {
            sb.append(" LIMIT " + update.limit.limit);
        }

        return new SQLBuilderCombine(sb.toString(), placeholders);
    }

    private void buildUpdateItem(MappingGlobalWrapper wrapper,
                                 StampUpdate update,
                                 StampUpdateItem item,
                                 StringBuilder sb,
                                 List<SQLDataPlaceholder> placeholders) {
        item.column.table = update.table.table;
        item.column.tableAliasName = update.table.aliasName;
        String name = this.getColumnName(wrapper, update, item.column);
        sb.append(name);
        sb.append(" = ");
        sb.append("?");
        placeholders.add(new SQLDataPlaceholder(name, item.value));
    }
}
