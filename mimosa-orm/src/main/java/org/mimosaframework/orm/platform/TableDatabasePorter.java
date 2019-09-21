package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;

import java.sql.SQLException;

public interface TableDatabasePorter {
    void createTable(MappingTable table) throws SQLException;

    void createField(MappingField field) throws SQLException;

    void updateField(MappingField field) throws SQLException;

    void dropField(String table, MappingField field) throws SQLException;

    void dropTable(String tableName) throws SQLException;
}
