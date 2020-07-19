package org.mimosaframework.orm.platform.sqlserver;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class SQLServerStampUpdate extends PlatformStampUpdate {
    public SQLServerStampUpdate(PlatformStampSection section,
                                PlatformStampReference reference,
                                PlatformDialect dialect,
                                PlatformStampShare share) {
        super(section, reference, dialect, share);
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampUpdate update = (StampUpdate) action;
        StringBuilder sb = new StringBuilder();
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();
        sb.append("UPDATE ");
        StampFrom fromTarget = update.table;
        if (fromTarget != null) {
            sb.append(this.reference.getTableName(wrapper, fromTarget.table, fromTarget.name));
            if (StringTools.isNotEmpty(fromTarget.aliasName)) {
                sb.append(" AS " + this.reference.getWrapStart() + fromTarget.aliasName + this.reference.getWrapEnd());
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
            this.share.buildWhere(wrapper, placeholders, update, update.where, sb);
        }

        return new SQLBuilderCombine(sb.toString(), placeholders);
    }

    private void buildUpdateItem(MappingGlobalWrapper wrapper,
                                 StampUpdate update,
                                 StampUpdateItem item,
                                 StringBuilder sb,
                                 List<SQLDataPlaceholder> placeholders) {
        item.column.table = null;
        item.column.tableAliasName = null;
        String name = this.reference.getColumnName(wrapper, update, item.column);
        sb.append(name);
        sb.append(" = ");

        if (item.value instanceof StampColumn) {
            StampColumn column = (StampColumn) item.value;
            sb.append(this.reference.getColumnName(wrapper, update, column));
        } else {
            sb.append("?");
            placeholders.add(new SQLDataPlaceholder(name, item.value));
        }
    }
}
