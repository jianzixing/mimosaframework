package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.mapping.MappingTable;

import java.sql.SQLException;
import java.util.List;

public interface InsertDatabasePorter {
    Long insert(MappingTable table, ModelObject object) throws SQLException;

    List<Long> inserts(MappingTable table, List<ModelObject> objects) throws SQLException;

    Long simpleInsert(String table, ModelObject object) throws SQLException;
}
