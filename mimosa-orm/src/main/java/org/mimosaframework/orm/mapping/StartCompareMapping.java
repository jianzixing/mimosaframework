package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MimosaDataSource;

import java.sql.SQLException;
import java.util.Map;

public interface StartCompareMapping {

    Map<MimosaDataSource, NotMatchObject> doMapping() throws SQLException;

    MappingGlobalWrapper getWholeMappingDatabase() throws SQLException;
}
