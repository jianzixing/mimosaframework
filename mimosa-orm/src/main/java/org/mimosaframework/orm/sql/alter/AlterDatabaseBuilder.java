package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.CharsetBuilder;
import org.mimosaframework.orm.sql.CollateBuilder;
import org.mimosaframework.orm.sql.UnifyBuilder;

public interface AlterDatabaseBuilder
        extends
        CharsetBuilder<CollateBuilder<UnifyBuilder>>,
        CollateBuilder<UnifyBuilder> {
}
