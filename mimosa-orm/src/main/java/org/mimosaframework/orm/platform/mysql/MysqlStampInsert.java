package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.platform.SQLDataPlaceholder;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class MysqlStampInsert extends MysqlAbstractStamp implements StampCombineBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampInsert insert = (StampInsert) action;
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT");
        sb.append(" INTO");
        sb.append(" " + this.getTableName(wrapper, insert.table, insert.name));

        StampColumn[] columns = insert.columns;
        String[] names = new String[columns.length];
        sb.append(" (");
        int i = 0;
        for (StampColumn column : columns) {
            String name = this.getColumnName(wrapper, insert, column);
            sb.append(name);
            names[i] = name;
            i++;
            if (i != columns.length) sb.append(",");
        }
        sb.append(")");

        sb.append(" VALUES ");

        Object[][] values = insert.values;
        int j = 0;
        for (Object[] row : values) {
            int k = 0;
            sb.append("(");
            for (Object v : row) {
                placeholders.add(new SQLDataPlaceholder(names[k], v));
                k++;
                if (k != row.length) sb.append(",");
            }
            sb.append(")");

            j++;
            if (j != values.length) sb.append(",");
        }

        return null;
    }
}
