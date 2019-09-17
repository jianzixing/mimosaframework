package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.mapping.SpecificMappingField;
import org.mimosaframework.orm.platform.DatabaseSpeciality;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleDatabaseSpeciality implements DatabaseSpeciality {
    private static final String[] TYPE = {"TABLE"};

    @Override
    public boolean isSupportGeneratedKeys() {
        return false;
    }

    @Override
    public ResultSet getTablesByMateData(Connection connection, DatabaseMetaData databaseMetaData) throws SQLException {
        return databaseMetaData.getTables(null, databaseMetaData.getUserName(), "%", TYPE);
    }

    /**
     * Oracle ResultSet 必须按照顺序读取
     * 如果get的顺序不正确就会报 流已关闭 的错误
     *
     * @param table
     * @param columnResultSet
     * @return
     * @throws SQLException
     */
    @Override
    public MappingField getDatabaseMappingField(MappingTable table, ResultSet columnResultSet) throws SQLException {
        String columnName = columnResultSet.getString("COLUMN_NAME");
        String typeName = columnResultSet.getString("TYPE_NAME");
        int dataType = columnResultSet.getInt("DATA_TYPE");
        int length = columnResultSet.getInt("COLUMN_SIZE");
        int decimalDigits = columnResultSet.getInt("DECIMAL_DIGITS");
        String defaultValue = columnResultSet.getString("COLUMN_DEF");
        String nullable = columnResultSet.getString("IS_NULLABLE"); // NULLABLE 非IOS规则
        String comment = columnResultSet.getString("REMARKS");

        MappingField mappingField = new SpecificMappingField(table);
        ((SpecificMappingField) mappingField).setDatabaseColumnName(columnName.trim());
        ((SpecificMappingField) mappingField).setDatabaseColumnTypeName(typeName);
        ((SpecificMappingField) mappingField).setDatabaseColumnDataType(dataType);
        ((SpecificMappingField) mappingField).setDatabaseColumnAutoIncrement("UNKNOWN");
        ((SpecificMappingField) mappingField).setDatabaseColumnLength(length);
        ((SpecificMappingField) mappingField).setDatabaseColumnDecimalDigits(decimalDigits);
        ((SpecificMappingField) mappingField).setDatabaseColumnNullable(nullable);
        ((SpecificMappingField) mappingField).setDatabaseColumnDefaultValue(defaultValue);
        ((SpecificMappingField) mappingField).setDatabaseColumnComment(comment);
        return mappingField;
    }
}