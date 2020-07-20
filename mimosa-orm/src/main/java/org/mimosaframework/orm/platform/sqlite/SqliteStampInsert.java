package org.mimosaframework.orm.platform.sqlite;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampInsert;

import java.util.ArrayList;
import java.util.List;

public class SqliteStampInsert extends PlatformStampInsert {
    public SqliteStampInsert(PlatformStampSection section,
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
            this.buildInsertSelect(wrapper, insert, sb, placeholders, new SqliteStampBuilder());
        } else {
            this.buildInsertValues(insert, sb, placeholders, names);
        }

        return new SQLBuilderCombine(sb.toString(), placeholders);
    }
}
