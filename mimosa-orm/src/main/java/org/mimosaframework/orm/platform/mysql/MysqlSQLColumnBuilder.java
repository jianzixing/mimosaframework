package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.platform.SQLBuilderFactory;
import org.mimosaframework.orm.sql.create.AbstractSQLColumnBuilder;
import org.mimosaframework.orm.utils.DatabaseTypes;

public class MysqlSQLColumnBuilder extends AbstractSQLColumnBuilder {
    @Override
    protected SQLBuilder createSQLBuilder() {
        return SQLBuilderFactory.createSQLBuilder(DatabaseTypes.MYSQL);
    }
}
