package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;
import org.mimosaframework.orm.platform.DatabaseSpeciality;
import org.mimosaframework.orm.platform.PlatformFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JDBCFetchDatabaseMapping implements FetchDatabaseMapping {
    private ActionDataSourceWrapper dataSourceWrapper;
    private Set<MimosaDataSource> mimosaDataSources = new LinkedHashSet<>();
    private Map<MimosaDataSource, MappingDatabase> mappingDatabases;
    private Map<MimosaDataSource, List<SupposedTables>> supposeds;

    @Override
    public void setDataSourceWrapper(ActionDataSourceWrapper dataSourceWrapper) {
        this.dataSourceWrapper = dataSourceWrapper;
    }

    @Override
    public Map<MimosaDataSource, MappingDatabase> loading() throws SQLException {
        this.resolveMimosaDatabase();
        mappingDatabases = new LinkedHashMap<>();
        Iterator<MimosaDataSource> ds = mimosaDataSources.iterator();
        while (ds.hasNext()) {
            MimosaDataSource mds = ds.next();
            MappingDatabase mappingDatabase = this.getMappingDatabase(mds);
            mappingDatabases.put(mds, mappingDatabase);
        }
        return mappingDatabases;
    }

    private MappingDatabase getMappingDatabase(MimosaDataSource mds) throws SQLException {
        ResultSet tableResultSet = null;
        ResultSet columnResultSet = null;

        DatabaseSpeciality speciality = PlatformFactory.getLocalSpeciality(mds);

        DataSource ds = mds.getMaster();
        Connection connection = null;
        try {
            connection = ds.getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            MappingDatabase mappingDatabase = new SpecificMappingDatabase(mds);
            try {
                tableResultSet = speciality.getTablesByMateData(connection, databaseMetaData);
                while (tableResultSet.next()) {
                    String tableName = tableResultSet.getString("TABLE_NAME");
                    MappingTable mappingTable = new SpecificMappingTable(mappingDatabase);
                    ((SpecificMappingTable) mappingTable).setDatabaseTableName(tableName.trim());
                    mappingDatabase.addDatabaseTable(mappingTable);
                }
            } finally {
                if (tableResultSet != null) {
                    tableResultSet.close();
                }
            }

            Set<MappingTable> mappingTables = mappingDatabase.getDatabaseTables();
            if (mappingTables != null) {
                for (MappingTable table : mappingTables) {
                    try {
                        String tableName = table.getDatabaseTableName();
                        columnResultSet = databaseMetaData.getColumns(connection.getCatalog(), "%", tableName, "%");
                        while (columnResultSet.next()) {
                            MappingField mappingField = speciality.getDatabaseMappingField(table, columnResultSet);
                            table.addDatabaseColumnField(mappingField);
                        }
                    } finally {
                        if (columnResultSet != null) {
                            columnResultSet.close();
                        }
                    }
                }
            }

            return mappingDatabase;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private void resolveMimosaDatabase() {
        //单机数据源
        MimosaDataSource defaultds = dataSourceWrapper.getDataSource();
        if (defaultds != null) {
            mimosaDataSources.add(defaultds);
        }
    }

    @Override
    public Map<MimosaDataSource, MappingDatabase> getDatabaseMapping() {
        return mappingDatabases;
    }

    @Override
    public Set<MimosaDataSource> getUseDataSources() {
        if (mimosaDataSources.size() == 0) {
            this.resolveMimosaDatabase();
        }
        return mimosaDataSources;
    }

    @Override
    public List<SupposedTables> getSupposedMappingTableByDataSource(MimosaDataSource dataSource) {
        if (mimosaDataSources.size() == 0) {
            this.resolveMimosaDatabase();
        }

        if (supposeds != null) {
            return supposeds.get(dataSource);
        }
        return null;
    }

    @Override
    public MappingDatabase getDatabaseMapping(MimosaDataSource dataSource) throws SQLException {
        MappingDatabase mappingDatabase = this.getMappingDatabase(dataSource);
        return mappingDatabase;
    }
}
