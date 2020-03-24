package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;

import java.io.Serializable;
import java.util.List;

public class SQLMappingField {
    private Class table;
    private String tableAliasName;
    private Serializable field;

    public SQLMappingField(Serializable field) {
        this.field = field;
    }

    public SQLMappingField(Class table, Serializable field) {
        this.table = table;
        this.field = field;
    }

    public SQLMappingField(String tableAliasName, Serializable field) {
        this.tableAliasName = tableAliasName;
        this.field = field;
    }

    public Class getTable() {
        return table;
    }

    public void setTable(Class table) {
        this.table = table;
    }

    public String getTableAliasName() {
        return tableAliasName;
    }

    public void setTableAliasName(String tableAliasName) {
        this.tableAliasName = tableAliasName;
    }

    public Serializable getField() {
        return field;
    }

    public void setField(Serializable field) {
        this.field = field;
    }

    public String getMayBeField(List sql, MappingGlobalWrapper mappingGlobalWrapper) {
        if (table != null) {
            MappingTable mappingTable = mappingGlobalWrapper.getMappingTable(table);
            if (mappingTable == null) {
                throw new IllegalArgumentException(
                        Messages.get(LanguageMessageFactory.PROJECT,
                                CommonSQLBuilder.class, "miss_table_mapping", table.getName())
                );
            }

            MappingField mappingField = mappingTable.getMappingFieldByName(field.toString());
            if (mappingField == null) {
                throw new IllegalArgumentException(
                        Messages.get(LanguageMessageFactory.PROJECT,
                                CommonSQLBuilder.class, "miss_field_mapping", table.getName(), field.toString())
                );
            }
            String field = mappingField.getMappingColumnName();
            return field;
        } else {
            for (Object o : sql) {
                if (o instanceof SQLMappingTable) {
                    MappingTable mappingTable = mappingGlobalWrapper.getMappingTable(((SQLMappingTable) o).getTable());
                    if (mappingTable != null) {
                        MappingField mappingField = mappingTable.getMappingFieldByJavaName(field.toString());
                        if (mappingField != null) {
                            return mappingField.getMappingColumnName();
                        }
                    }
                }
            }
        }
        return this.field.toString();
    }
}
