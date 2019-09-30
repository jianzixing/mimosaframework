package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.mapping.SpecificMappingField;
import org.mimosaframework.orm.platform.DatabaseSpeciality;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB2DatabaseSpeciality implements DatabaseSpeciality {
    private static final String[] TYPE = {"TABLE"};

    @Override
    public boolean isSupportGeneratedKeys() {
        return true;
    }

    @Override
    public ResultSet getTablesByMateData(Connection connection, DatabaseMetaData databaseMetaData) throws SQLException {
        return databaseMetaData.getTables(connection.getCatalog(), null, "%", TYPE);
    }

    @Override
    public MappingField getDatabaseMappingField(MappingTable table, ResultSet columnResultSet) throws SQLException {
        String columnName = columnResultSet.getString("COLUMN_NAME");
        String typeName = columnResultSet.getString("TYPE_NAME");
        int dataType = columnResultSet.getInt("DATA_TYPE");
        String isAutoincrement = columnResultSet.getString("IS_AUTOINCREMENT");
        int length = columnResultSet.getInt("COLUMN_SIZE");
        int decimalDigits = columnResultSet.getInt("DECIMAL_DIGITS");
        String nullable = columnResultSet.getString("IS_NULLABLE"); // NULLABLE 非IOS规则
        String defaultValue = columnResultSet.getString("COLUMN_DEF");
        String comment = columnResultSet.getString("REMARKS");

        MappingField mappingField = new SpecificMappingField(table);
        ((SpecificMappingField) mappingField).setDatabaseColumnName(columnName.trim());
        ((SpecificMappingField) mappingField).setDatabaseColumnTypeName(typeName);
        ((SpecificMappingField) mappingField).setDatabaseColumnDataType(dataType);
        ((SpecificMappingField) mappingField).setDatabaseColumnAutoIncrement(isAutoincrement);
        ((SpecificMappingField) mappingField).setDatabaseColumnLength(length);
        ((SpecificMappingField) mappingField).setDatabaseColumnDecimalDigits(decimalDigits);
        ((SpecificMappingField) mappingField).setDatabaseColumnNullable(nullable);
        ((SpecificMappingField) mappingField).setDatabaseColumnDefaultValue(defaultValue);
        ((SpecificMappingField) mappingField).setDatabaseColumnComment(comment);
        return mappingField;
    }

    @Override
    public boolean isUserTable(ResultSet resultSet) {
        return true;
    }

    @Override
    public void loadMappingColumns(Connection connection, DatabaseMetaData databaseMetaData, MappingTable table) throws SQLException {
        ResultSet columnResultSet = null;
        try {
            String tableName = table.getDatabaseTableName();
            columnResultSet = databaseMetaData.getColumns(connection.getCatalog(), "%", tableName, "%");
            while (columnResultSet.next()) {
                MappingField mappingField = getDatabaseMappingField(table, columnResultSet);
                table.addDatabaseColumnField(mappingField);
            }
        } finally {
            if (columnResultSet != null) {
                columnResultSet.close();
            }
        }
    }
}
