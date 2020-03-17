package org.mimosaframework.orm.sql.test.delete;

import org.mimosaframework.orm.sql.test.DeleteBuilder;

public class DeleteFactory {
    public static DeleteStartBuilder delete() {
        DeleteBuilder<DeleteStartBuilder> deleteBuilder = null;
        return deleteBuilder.delete();
    }
}
