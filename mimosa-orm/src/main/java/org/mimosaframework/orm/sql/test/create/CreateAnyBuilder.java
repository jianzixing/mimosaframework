package org.mimosaframework.orm.sql.test.create;

import org.mimosaframework.orm.sql.test.DatabaseBuilder;
import org.mimosaframework.orm.sql.test.TableBuilder;

public interface CreateAnyBuilder
        extends
        TableBuilder<CreateTableStartBuilder>,
        DatabaseBuilder<CreateDatabaseStartBuilder> {
}
