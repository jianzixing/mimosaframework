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
        StampUpdateItem[] items = update.items;

        StampFrom fromTarget = update.table;
        if (fromTarget != null) {
            sb.append(this.getTableName(wrapper, fromTarget.table, fromTarget.name));
            if (StringTools.isNotEmpty(fromTarget.aliasName)) {
                sb.append(" AS " + RS + fromTarget.aliasName + RE);
            }
        }

        sb.append(" SET ");
        int k = 0;
        for (StampUpdateItem item : items) {
            this.buildUpdateItem(wrapper, update, item, sb, placeholders);
            k++;
            if (k != items.length) sb.append(",");
        }

        sb.append(" WHERE ");
        this.buildWhere(wrapper, placeholders, update, update.where, sb);

        return new SQLBuilderCombine(sb.toString(), placeholders);
    }

    private void buildUpdateItem(MappingGlobalWrapper wrapper,
                                 StampUpdate update,
                                 StampUpdateItem item,
                                 StringBuilder sb,
                                 List<SQLDataPlaceholder> placeholders) {
        item.column.table = null;
        item.column.tableAliasName = null;
        String name = this.getColumnName(wrapper, update, item.column);
        sb.append(name);
        sb.append(" = ");

        if (item.value instanceof StampColumn) {
            StampColumn column = (StampColumn) item.value;
            column.table = null;
            column.tableAliasName = null;
            sb.append(this.getColumnName(wrapper, update, column));
        } else {
            sb.append("?");
            placeholders.add(new SQLDataPlaceholder(name, item.value));
        }
    }
}
