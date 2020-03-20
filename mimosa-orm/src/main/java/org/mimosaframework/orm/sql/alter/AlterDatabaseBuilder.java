package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.CharsetBuilder;
import org.mimosaframework.orm.sql.CollateBuilder;

public interface AlterDatabaseBuilder
        extends
        CharsetBuilder<CollateBuilder>,
        CollateBuilder {
}
