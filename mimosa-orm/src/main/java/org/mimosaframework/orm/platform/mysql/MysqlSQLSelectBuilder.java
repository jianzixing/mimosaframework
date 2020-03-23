package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.platform.SQLBuilderFactory;
import org.mimosaframework.orm.sql.select.AbstractSQLSelectBuilder;
import org.mimosaframework.orm.utils.DatabaseTypes;

public class MysqlSQLSelectBuilder extends AbstractSQLSelectBuilder {
    @Override
    protected SQLBuilder createSQLBuilder() {
        return SQLBuilderFactory.createSQLBuilder(DatabaseTypes.MYSQL);
    }
}
