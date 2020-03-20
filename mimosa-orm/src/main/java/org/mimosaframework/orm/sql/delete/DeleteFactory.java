package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.DeleteBuilder;

public class DeleteFactory {
    public static DeleteStartBuilder delete() {
        DeleteBuilder<DeleteStartBuilder> deleteBuilder = new AbstractSQLDeleteBuilder();
        return deleteBuilder.delete();
    }
}
