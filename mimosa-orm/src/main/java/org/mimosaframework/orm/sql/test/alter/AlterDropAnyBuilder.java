package org.mimosaframework.orm.sql.test.alter;

import org.mimosaframework.orm.sql.test.AbsNameBuilder;
import org.mimosaframework.orm.sql.test.ColumnBuilder;
import org.mimosaframework.orm.sql.test.IndexBuilder;
import org.mimosaframework.orm.sql.test.PrimaryKeyBuilder;

public interface AlterDropAnyBuilder
        extends
        ColumnBuilder<AbsNameBuilder>,
        IndexBuilder<AbsNameBuilder>,
        PrimaryKeyBuilder<Void> {
}
