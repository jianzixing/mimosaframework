package org.mimosaframework.orm.sql.test.alter;

import org.mimosaframework.orm.sql.test.AbsNameBuilder;
import org.mimosaframework.orm.sql.test.DatabaseBuilder;
import org.mimosaframework.orm.sql.test.TableBuilder;

public interface AlterAnyBuilder
        extends
        DatabaseBuilder<AbsNameBuilder<AlterDatabaseBuilder>>,
        TableBuilder {
}
