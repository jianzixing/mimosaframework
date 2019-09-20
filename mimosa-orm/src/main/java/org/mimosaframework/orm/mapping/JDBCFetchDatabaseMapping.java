package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MimosaConnection;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.platform.DatabaseSpeciality;
import org.mimosaframework.orm.platform.PlatformFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class JDBCFetchDatabaseMapping implements FetchDatabaseMapping {
    private MimosaDataSource mimosaDataSource;
    private MappingDatabase mappingDatabase;

    public JDBCFetchDatabaseMapping(MimosaDataSource mimosaDataSource) {
        this.mimosaDataSource = mimosaDataSource;
    }

    @Override
    public void loading() throws SQLException {
        this.mappingDatabase = this.getMappingDatabase(mimosaDataSource);
    }

    private MappingDatabase getMappingDatabase(MimosaDataSource mds) throws SQLException {
        ResultSet tableResultSet = null;
        ResultSet columnResultSet = null;

        DatabaseSpeciality speciality = PlatformFactory.getLocalSpeciality(mds);

        DataSource ds = mds.getMaster();
        Connection connection = null;
        try {
            connection = MimosaConnection.getConnection(ds);
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            MappingDatabase mappingDatabase = new SpecificMappingDatabase(mds);
            try {
                tableResultSet = speciality.getTablesByMateData(connection, databaseMetaData);
                while (tableResultSet.next()) {
                    if (speciality.isUserTable(tableResultSet)) {
                        String tableName = tableResultSet.getString("TABLE_NAME");
                        MappingTable mappingTable = new SpecificMappingTable(mappingDatabase);
                        ((SpecificMappingTable) mappingTable).setDatabaseTableName(tableName.trim());
                        mappingDatabase.addDatabaseTable(mappingTable);
                    }
                }
            } finally {
                if (tableResultSet != null) {
                    tableResultSet.close();
                }
            }

            Set<MappingTable> mappingTables = mappingDatabase.getDatabaseTables();
            if (mappingTables != null) {
                for (MappingTable table : mappingTables) {
                    speciality.loadMappingColumns(connection, databaseMetaData, table);
                }
            }

            return mappingDatabase;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public MappingDatabase getDatabaseMapping() {
        return mappingDatabase;
    }
}
