package org.mimosaframework.orm.mapping;

import java.sql.SQLException;

public interface FetchDatabaseMapping {
    /**
     * 每调用一次就是查询一次数据库
     *
     * @throws SQLException
     */
    void loading() throws SQLException;

    MappingDatabase getDatabaseMapping();
}
