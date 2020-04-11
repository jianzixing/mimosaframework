package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.AbsNameBuilder;
import org.mimosaframework.orm.sql.AbsTableBuilder;
import org.mimosaframework.orm.sql.DatabaseBuilder;

public interface AlterAnyBuilder
        extends
        DatabaseBuilder<AbsNameBuilder<AlterDatabaseBuilder>>,
        AlterTableNameBuilder<AlterTableOptionBuilder> {
}
