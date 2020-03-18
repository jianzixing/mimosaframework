package org.mimosaframework.orm.sql.test.insert;

import org.mimosaframework.orm.sql.test.InsertBuilder;

public class InsertFactory {
    public static InsertStartBuilder insert() {
        InsertBuilder<InsertStartBuilder> insertBuilder = null;
        return insertBuilder.insert();
    }
}
