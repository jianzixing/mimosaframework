package org.mimosaframework.orm.platform.sqlserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.mapping.SpecificMappingField;
import org.mimosaframework.orm.platform.DatabaseSpeciality;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLServerDatabaseSpeciality implements DatabaseSpeciality {
    private static final Log logger = LogFactory.getLog(SQLServerDatabaseSpeciality.class);
    private static final String[] TYPE = {"TABLE"};

    @Override
    public boolean isSupportGeneratedKeys() {
        return true;
    }

    @Override
    public ResultSet getTablesByMateData(Connection connection, DatabaseMetaData databaseMetaData) throws SQLException {
        return databaseMetaData.getTables(connection.getCatalog(), null, "%", TYPE);
    }

    /**
     * https://docs.microsoft.com/en-us/sql/connect/jdbc/reference/getcolumns-method-sqlserverdatabasemetadata?view=sql-server-2017
     * 具体值参考这里
     *
     * @param table
     * @param columnResultSet
     * @return
     * @throws SQLException
     */
    @Override
    public MappingField getDatabaseMappingField(MappingTable table, ResultSet columnResultSet) throws SQLException {
        MappingField mappingField = new SpecificMappingField(table);

        String columnName = columnResultSet.getString("COLUMN_NAME");
        String typeName = columnResultSet.getString("TYPE_NAME");
        int dataType = columnResultSet.getInt("DATA_TYPE");
        try {
            String isAutoincrement = columnResultSet.getString("IS_AUTOINCREMENT");
        } catch (Exception e) {
        }
        int length = columnResultSet.getInt("COLUMN_SIZE");
        int decimalDigits = columnResultSet.getInt("DECIMAL_DIGITS");
        String nullable = columnResultSet.getString("IS_NULLABLE"); // NULLABLE 非IOS规则
        String defaultValue = columnResultSet.getString("COLUMN_DEF");
        String comment = columnResultSet.getString("REMARKS");

        return mappingField;
    }

    @Override
    public boolean isUserTable(ResultSet resultSet) throws SQLException {
        return true;
    }

    @Override
    public void loadMappingColumns(Connection connection, DatabaseMetaData databaseMetaData, MappingTable table) throws SQLException {

    }
}
