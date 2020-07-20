package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.platform.db2.DB2StampBuilder;
import org.mimosaframework.orm.platform.db2.DB2StampSelect;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampColumn;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;
import org.mimosaframework.orm.sql.stamp.StampInsert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class OracleStampInsert extends PlatformStampInsert {
    public OracleStampInsert(PlatformStampSection section,
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
        Object[][] values = insert.values;
        if (values != null && values.length > 1) {
            sb.append(this.section.getNl() + "BEGIN");
            sb.append(" " + this.section.getNTab());
        }
        sb.append("INSERT");
        if (values != null && values.length > 1) sb.append(" ALL");
        sb.append(" INTO");

        String tableName = this.reference.getTableName(wrapper, insert.tableClass, insert.tableName);
        sb.append(" " + this.reference.getTableName(wrapper, insert.tableClass, insert.tableName));

        boolean isContains = false;
        StampColumn[] columns = insert.columns;
        String[] names = null;
        if (columns != null && columns.length > 0) {
            names = new String[columns.length];

            if (insert.autoField != null && insert.autoField.type == 1) {
                for (StampColumn column : columns) {
                    if (column.column.equals(insert.autoField.columnName)) {
                        isContains = true;
                    }
                }
            }

            sb.append(" (");
            int i = 0;
            if (!isContains && insert.autoField != null && insert.autoField.type == 1) {
                sb.append(this.reference.getColumnName(wrapper, insert, new StampColumn(insert.autoField.columnName)));
                if (columns != null && columns.length > 0) {
                    sb.append(",");
                }
            }
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

        for (Object[] row : values) {
            int k = 0;
            sb.append("(");
            if (!isContains && insert.autoField != null && insert.autoField.type == 1) {
                sb.append(tableName + "_SEQ.NEXTVAL");
                if (row != null && row.length > 0) sb.append(",");
            }
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
        }

        if (insert.select != null) {
            this.buildInsertSelect(wrapper, insert, sb, placeholders, new OracleStampBuilder());
        }

        if (values != null && values.length > 1) {
            sb.append(" " + this.section.getNTab() + "SELECT 1 FROM DUAL;");
            sb.append(this.section.getNl() + "END;");
        }
        return new SQLBuilderCombine(sb.toString(), placeholders);
    }
}
