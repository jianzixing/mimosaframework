package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;

public interface TableDatabasePorter {
    PorterStructure[] createTable(MappingTable table);

    PorterStructure[] createField(MappingField field);

    PorterStructure[] updateField(MappingField field);

    PorterStructure[] dropField(String table, MappingField field);

    PorterStructure[] dropTable(String tableName);
}
