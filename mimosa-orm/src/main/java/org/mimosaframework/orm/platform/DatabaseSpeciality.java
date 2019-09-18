package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseSpeciality {
    /**
     * 数据库驱动是否支持返回添加后的ID
     *
     * @return
     */
    boolean isSupportGeneratedKeys();

    ResultSet getTablesByMateData(Connection connection, DatabaseMetaData databaseMetaData) throws SQLException;

    MappingField getDatabaseMappingField(MappingTable table, ResultSet columnResultSet) throws SQLException;

    boolean isUserTable(ResultSet resultSet) throws SQLException;

    void loadMappingColumns(Connection connection, DatabaseMetaData databaseMetaData, MappingTable table) throws SQLException;
}
