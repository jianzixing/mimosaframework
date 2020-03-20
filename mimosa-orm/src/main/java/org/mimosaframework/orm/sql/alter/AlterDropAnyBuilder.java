package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.AbsNameBuilder;
import org.mimosaframework.orm.sql.ColumnBuilder;
import org.mimosaframework.orm.sql.IndexBuilder;
import org.mimosaframework.orm.sql.PrimaryKeyBuilder;

public interface AlterDropAnyBuilder
        extends
        ColumnBuilder<AbsNameBuilder>,
        IndexBuilder<AbsNameBuilder>,
        PrimaryKeyBuilder<Void> {
}
