package org.mimosaframework.orm.sql.drop;

import org.mimosaframework.orm.sql.SQLActionFactory;

public class DropFactory {
    public static DropAnyBuilder drop() {
        return SQLActionFactory.drop();
    }
}
