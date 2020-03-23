package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.sql.AbstractSQLBuilder;
import org.mimosaframework.orm.sql.UnifyBuilder;

import java.sql.SQLException;

public interface DatabasePorter extends
        TableDatabasePorter,
        InsertDatabasePorter,
        UpdateDatabasePorter,
        DeleteDatabasePorter,
        SelectDatabasePorter {

    void setCarryHandler(CarryHandler carryHandler);

    Object execute(MappingGlobalWrapper mappingGlobalWrapper, AbstractSQLBuilder builder) throws SQLException;
}
