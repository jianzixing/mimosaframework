package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.SQLActionFactory;

public class SelectFactory {
    public static SelectStartBuilder select() {
        return SQLActionFactory.select();
    }

    public static DefaultSQLSelectBuilder origin() {
        return new DefaultSQLSelectBuilder();
    }
}
