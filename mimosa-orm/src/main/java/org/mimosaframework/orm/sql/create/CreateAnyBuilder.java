package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.DatabaseBuilder;
import org.mimosaframework.orm.sql.TableBuilder;

public interface CreateAnyBuilder
        extends
        TableBuilder<CreateTableStartBuilder>,
        DatabaseBuilder<CreateDatabaseStartBuilder>,
        CreateIndexBuilder {
}
