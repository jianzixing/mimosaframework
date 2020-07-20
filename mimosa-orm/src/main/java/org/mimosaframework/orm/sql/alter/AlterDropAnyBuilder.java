package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.AbsColumnBuilder;
import org.mimosaframework.orm.sql.PrimaryKeyBuilder;

public interface AlterDropAnyBuilder<T>
        extends
        AbsColumnBuilder<T>,
        PrimaryKeyBuilder<T> {
}
