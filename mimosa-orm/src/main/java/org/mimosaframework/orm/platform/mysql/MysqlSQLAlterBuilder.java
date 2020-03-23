package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.platform.SQLBuilderFactory;
import org.mimosaframework.orm.sql.alter.AbstractSQLAlterBuilder;
import org.mimosaframework.orm.utils.DatabaseTypes;

public class MysqlSQLAlterBuilder extends AbstractSQLAlterBuilder {
    @Override
    protected SQLBuilder createSQLBuilder() {
        return SQLBuilderFactory.createSQLBuilder(DatabaseTypes.MYSQL);
    }
}
