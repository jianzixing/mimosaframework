package org.mimosaframework.orm.sql.test.alter;

import org.mimosaframework.orm.sql.test.AbsNameBuilder;
import org.mimosaframework.orm.sql.test.AbsTableBuilder;
import org.mimosaframework.orm.sql.test.DatabaseBuilder;

public interface AlterAnyBuilder
        extends
        DatabaseBuilder<AbsNameBuilder<AlterDatabaseBuilder>>,
        AbsTableBuilder<AlterTableOptionBuilder> {
}
