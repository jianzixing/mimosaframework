package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.platform.SQLDataPlaceholder;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class MysqlStampUpdate extends MysqlAbstractStamp implements StampUpdateBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampUpdate update) {
        StringBuilder sb = new StringBuilder();
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();
        sb.append("UPDATE ");
        StampFrom[] tables = update.tables;
        int i = 0;
        for (StampFrom table : tables) {
            sb.append(this.getTableName(wrapper, table.table, table.name));
            if (StringTools.isNotEmpty(table.aliasName)) {
                sb.append(" " + RS + table.aliasName + RE);
            }
            i++;
            if (i != tables.length) sb.append(",");
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

        if (update.orderBy != null) {
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
            sb.append(" LIMIT " + update.limit.start + "," + update.limit.limit);
        }

        return null;
    }

    private void buildUpdateItem(MappingGlobalWrapper wrapper,
                                 StampUpdate update,
                                 StampUpdateItem item,
                                 StringBuilder sb,
                                 List<SQLDataPlaceholder> placeholders) {
        String name = this.getColumnName(wrapper, update, item.column);
        sb.append(name);
        sb.append("=");
        sb.append("?");
        placeholders.add(new SQLDataPlaceholder(name, item.value));
    }
}
