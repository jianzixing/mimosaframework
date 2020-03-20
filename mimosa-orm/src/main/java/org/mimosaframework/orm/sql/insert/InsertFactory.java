package org.mimosaframework.orm.sql.insert;

import org.mimosaframework.orm.sql.InsertBuilder;

public class InsertFactory {
    public static InsertStartBuilder insert() {
        InsertBuilder<InsertStartBuilder> insertBuilder = null;
        return insertBuilder.insert();
    }
}
