package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingTable;

import java.sql.SQLException;

public interface PlatformCompare {
    void checking(CompareUpdateTableMate tableMate) throws SQLException;

    void start(MappingTable mappingTable);
}
