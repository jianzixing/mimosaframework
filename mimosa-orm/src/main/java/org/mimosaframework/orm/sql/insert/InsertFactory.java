package org.mimosaframework.orm.sql.insert;

import org.mimosaframework.orm.sql.SQLActionFactory;

public class InsertFactory {
    public static InsertStartBuilder insert() {
        return SQLActionFactory.insert();
    }
}
