package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.platform.SQLBuilderFactory;
import org.mimosaframework.orm.sql.delete.AbstractSQLDeleteBuilder;
import org.mimosaframework.orm.utils.DatabaseTypes;

public class MysqlSQLDeleteBuilder extends AbstractSQLDeleteBuilder {

    @Override
    protected SQLBuilder createSQLBuilder() {
        return SQLBuilderFactory.createSQLBuilder(DatabaseTypes.MYSQL);
    }

    @Override
    public MappingTable getMappingTableByClass(Class table) {
        return null;
    }
}
