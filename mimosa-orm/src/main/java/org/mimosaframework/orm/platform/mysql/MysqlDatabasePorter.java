package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.platform.AbstractDatabasePorter;
import org.mimosaframework.orm.platform.DifferentColumn;
import org.mimosaframework.orm.utils.DatabaseTypes;

public class MysqlDatabasePorter extends AbstractDatabasePorter {
    private static DifferentColumn differentColumn = new MysqlDifferentColumn();

    @Override
    protected DifferentColumn getDifferentColumn() {
        return differentColumn;
    }

    @Override
    protected DatabaseTypes getDatabaseType() {
        return DatabaseTypes.MYSQL;
    }
}
