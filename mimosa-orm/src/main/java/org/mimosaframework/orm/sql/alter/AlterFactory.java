package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.SQLActionFactory;

public class AlterFactory {
    public static AlterAnyBuilder alter() {
        return SQLActionFactory.alter();
    }

    public static DefaultSQLAlterBuilder origin() {
        return new DefaultSQLAlterBuilder();
    }
}
