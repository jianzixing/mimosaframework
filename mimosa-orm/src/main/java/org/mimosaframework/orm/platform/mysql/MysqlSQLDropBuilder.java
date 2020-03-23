package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.platform.SQLBuilderFactory;
import org.mimosaframework.orm.sql.drop.AbstractSQLDropBuilder;
import org.mimosaframework.orm.utils.DatabaseTypes;

public class MysqlSQLDropBuilder extends AbstractSQLDropBuilder {
    @Override
    protected SQLBuilder createSQLBuilder() {
        return SQLBuilderFactory.createSQLBuilder(DatabaseTypes.MYSQL);
    }
}
