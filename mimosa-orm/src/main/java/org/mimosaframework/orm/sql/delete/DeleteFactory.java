package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.SQLActionFactory;

public class DeleteFactory {
    public static DeleteStartBuilder delete() {
        return SQLActionFactory.delete();
    }

    public static DefaultSQLDeleteBuilder origin() {
        return new DefaultSQLDeleteBuilder();
    }
}
