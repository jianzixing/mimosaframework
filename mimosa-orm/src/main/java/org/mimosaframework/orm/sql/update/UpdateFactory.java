package org.mimosaframework.orm.sql.update;

import org.mimosaframework.orm.sql.SQLActionFactory;

public class UpdateFactory {
    public static UpdateStartBuilder update() {
        return SQLActionFactory.update();
    }

    public static DefaultSQLUpdateBuilder origin() {
        return new DefaultSQLUpdateBuilder();
    }
}
