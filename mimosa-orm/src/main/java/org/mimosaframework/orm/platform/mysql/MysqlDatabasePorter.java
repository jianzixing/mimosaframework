package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.platform.*;

public class MysqlDatabasePorter extends AbstractDatabasePorter {
    private static DifferentColumn differentColumn = new MysqlDifferentColumn();

    @Override
    protected DifferentColumn getDifferentColumn() {
        return differentColumn;
    }
}
