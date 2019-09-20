package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;

import java.sql.SQLException;
import java.util.*;

public class NothingCompareMapping implements StartCompareMapping {
    protected ActionDataSourceWrapper dataSourceWrapper;
    protected Set<MappingTable> mappingTables;

    public NothingCompareMapping(ActionDataSourceWrapper dataSourceWrapper, Set<MappingTable> mappingTables) {
        this.dataSourceWrapper = dataSourceWrapper;
        this.mappingTables = mappingTables;
    }

    @Override
    public NotMatchObject doMapping() throws SQLException {
        MappingTableWrapper tableWrapper = new MappingTableWrapper(mappingTables);

        MimosaDataSource dataSource = this.dataSourceWrapper.getDataSource();
        FetchDatabaseMapping fetchDatabaseMapping = new JDBCFetchDatabaseMapping(dataSource);
        fetchDatabaseMapping.loading();

        // 检查全部表映射
        MappingDatabase mappingDatabase = fetchDatabaseMapping.getDatabaseMapping();
        if (mappingDatabase != null) {
            NotMatchObject missing = tableWrapper.getMissingObject(dataSource, mappingDatabase);
            return missing;
        }

        return null;
    }
}
