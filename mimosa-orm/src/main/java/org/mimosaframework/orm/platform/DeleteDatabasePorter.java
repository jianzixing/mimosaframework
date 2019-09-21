package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.criteria.DefaultDelete;
import org.mimosaframework.orm.mapping.MappingTable;

import java.sql.SQLException;

public interface DeleteDatabasePorter {
    Integer delete(MappingTable table, ModelObject object) throws SQLException;

    Integer delete(MappingTable table, DefaultDelete delete) throws SQLException;

    Integer simpleDelete(String table, ModelObject where) throws SQLException;
}
