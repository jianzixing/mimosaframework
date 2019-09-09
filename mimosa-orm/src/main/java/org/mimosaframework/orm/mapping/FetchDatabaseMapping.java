package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FetchDatabaseMapping {
    void setDataSourceWrapper(ActionDataSourceWrapper dataSourceWrapper);

    /**
     * 每调用一次就是查询一次数据库
     *
     * @throws SQLException
     */
    Map<MimosaDataSource, MappingDatabase> loading() throws SQLException;

    Map<MimosaDataSource, MappingDatabase> getDatabaseMapping();

    Set<MimosaDataSource> getUseDataSources();

    List<SupposedTables> getSupposedMappingTableByDataSource(MimosaDataSource dataSource);

    MappingDatabase getDatabaseMapping(MimosaDataSource dataSource) throws SQLException;
}
