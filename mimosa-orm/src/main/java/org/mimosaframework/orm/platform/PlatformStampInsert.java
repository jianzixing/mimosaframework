package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.sql.stamp.StampColumn;
import org.mimosaframework.orm.sql.stamp.StampInsert;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class PlatformStampInsert extends PlatformStampCommonality {
    public PlatformStampInsert(PlatformStampSection section, PlatformStampReference reference, PlatformDialect dialect, PlatformStampShare share) {
        super(section, reference, dialect, share);
    }

    protected String[] buildInsertFields(MappingGlobalWrapper wrapper,
                                         StampInsert insert,
                                         StringBuilder sb) {
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
        return names;
    }

    protected void buildInsertSelect(MappingGlobalWrapper wrapper,
                                     StampInsert insert,
                                     StringBuilder sb,
                                     List<SQLDataPlaceholder> placeholders,
                                     PlatformStampBuilder builder) {
        sb.append(" ");
        SQLBuilderCombine combine = builder.select().getSqlBuilder(wrapper, insert.select);
        sb.append(combine.getSql());
        if (combine.getPlaceholders() != null) {
            placeholders.addAll(combine.getPlaceholders());
        }
    }

    protected void buildInsertValues(StampInsert insert,
                                     StringBuilder sb,
                                     List<SQLDataPlaceholder> placeholders,
                                     String[] names) {
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
}
