package org.mimosaframework.orm.sql.create;


import org.mimosaframework.orm.sql.SQLActionFactory;

public class CreateFactory {
    public static CreateAnyBuilder create() {
        return SQLActionFactory.create();
    }

    public static DefaultSQLCreateBuilder origin() {
        return new DefaultSQLCreateBuilder();
    }
}
