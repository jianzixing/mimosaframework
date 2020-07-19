package org.mimosaframework.orm.platform.postgresql;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.platform.db2.DB2StampSelect;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampColumn;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;
import org.mimosaframework.orm.sql.stamp.StampInsert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PostgreSQLStampInsert extends PlatformStampInsert {
    public PostgreSQLStampInsert(PlatformStampSection section,
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

        StampColumn[] columns = insert.columns;
        String[] names = null;
        if (columns != null && columns.length > 0) {
            names = new String[columns.length];
            sb.append(" (");
            int i = 0;
            for (StampColumn column : columns) {
                String name = this.reference.getColumnName(wrapper, insert, column);
                sb.append(name);
                names[i] = name;
                i++;
                if (i != columns.length) sb.append(",");
            }
            sb.append(")");

            if (insert.values != null && insert.values.length > 0) {
                sb.append(" VALUES ");
            }
        } else {
            if (insert.tableClass != null) {
                MappingTable mappingTable = wrapper.getMappingTable(insert.tableClass);
                if (mappingTable != null) {
                    Set<MappingField> fields = mappingTable.getMappingFields();
                    names = new String[fields.size()];
                    Iterator<MappingField> iterator = fields.iterator();
                    int c = 0;
                    while (iterator.hasNext()) {
                        MappingField field = iterator.next();
                        names[c] = field.getMappingFieldName();
                        c++;
                    }
                }
            }
        }

        if (insert.select != null) {
            sb.append(" ");
            SQLBuilderCombine combine = (new PostgreSQLStampBuilder().select()).getSqlBuilder(wrapper, insert.select);
            sb.append(combine.getSql());
            if (combine.getPlaceholders() != null) {
                placeholders.addAll(combine.getPlaceholders());
            }
        } else {
            Object[][] values = insert.values;
            int j = 0;
            for (Object[] row : values) {
                int k = 0;
                sb.append("(");
                for (Object v : row) {
                    String name = null;
                    if (names.length > k) name = names[k];
                    else name = "value&" + k;
                    sb.append("?");
                    placeholders.add(new SQLDataPlaceholder(name, v));
                    k++;
                    if (k != row.length) sb.append(",");
                }
                sb.append(")");

                j++;
                if (j != values.length) sb.append(",");
            }
        }

        return new SQLBuilderCombine(sb.toString(), placeholders);
    }
}
