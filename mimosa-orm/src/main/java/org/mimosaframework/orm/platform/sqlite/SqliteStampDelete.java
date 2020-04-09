package org.mimosaframework.orm.platform.sqlite;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.platform.SQLDataPlaceholder;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;
import org.mimosaframework.orm.sql.stamp.StampDelete;
import org.mimosaframework.orm.sql.stamp.StampFrom;

import java.util.ArrayList;
import java.util.List;

public class SqliteStampDelete extends SqliteStampCommonality implements StampCombineBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampDelete delete = (StampDelete) action;
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ");
        sb.append("FROM ");

        StampFrom from = delete.from;
        sb.append(this.getTableName(wrapper, from.table, from.name));
        if (StringTools.isNotEmpty(from.aliasName)) {
            sb.append(" AS " + from.aliasName);
        }

        if (delete.where != null) {
            sb.append(" WHERE ");
            this.buildWhere(wrapper, placeholders, delete, delete.where, sb);
        }

        return new SQLBuilderCombine(sb.toString(), placeholders);
    }
}
