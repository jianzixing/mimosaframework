package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.List;

public class MysqlStampSave extends PlatformStampInsert {
    public MysqlStampSave(PlatformStampSection section,
                          PlatformStampReference reference,
                          PlatformDialect dialect,
                          PlatformStampShare share) {
        super(section, reference, dialect, share);
    }

    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampInsert insert = (StampInsert) action;
        List<SQLDataPlaceholder> placeholders = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT");
        sb.append(" INTO");
        sb.append(" " + this.reference.getTableName(wrapper, insert.tableClass, insert.tableName));

        String[] names = this.buildInsertFields(wrapper, insert, sb);
        if (insert.select != null) {
            this.buildInsertSelect(wrapper, insert, sb, placeholders, new MysqlStampBuilder());
        } else {
            this.buildInsertValues(insert, sb, placeholders, names);
        }
        sb.append(" ON DUPLICATE KEY UPDATE ");
        this.buildUpdateSet(wrapper, insert, sb, placeholders);
        return new SQLBuilderCombine(sb.toString(), placeholders);
    }

    protected void buildUpdateSet(MappingGlobalWrapper wrapper,
                                  StampInsert insert,
                                  StringBuilder sb,
                                  List<SQLDataPlaceholder> placeholders) {
        Object[][] items = insert.values;
        if (items.length > 1) {
            throw new RuntimeException("not support multi values");
        }
        StampColumn[] columns = insert.columns;
        int i = 0;
        for (Object o : items[0]) {
            StampColumn column = columns[i];
            String name = this.reference.getColumnName(wrapper, insert, column);
            sb.append(name);
            sb.append(" = ");

            sb.append("?");
            placeholders.add(new SQLDataPlaceholder(name, o));
            i++;
            if (i != items[0].length) {
                sb.append(",");
            }
        }
    }
}
