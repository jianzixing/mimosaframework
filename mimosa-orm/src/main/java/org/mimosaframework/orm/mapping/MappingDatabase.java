package org.mimosaframework.orm.mapping;

import java.util.Set;

public interface MappingDatabase {
    void addDatabaseTable(MappingTable table);

    void addMappingTable(MappingTable table);

    MappingTable getDatabaseTable(String tableName);

    Set<MappingTable> getDatabaseTables();

    MappingTable[] getDatabaseTables(Class c);

    MappingTable[] getDatabaseTables(String dbTableName);
}
