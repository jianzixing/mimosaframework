package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.criteria.DefaultUpdate;
import org.mimosaframework.orm.mapping.MappingTable;

import java.sql.SQLException;

public interface UpdateDatabasePorter {
    Integer update(MappingTable table, ModelObject object) throws SQLException;

    Integer update(MappingTable table, DefaultUpdate update) throws SQLException;
}
