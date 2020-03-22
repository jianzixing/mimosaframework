package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.*;

public interface AlterDropAnyBuilder<T>
        extends
        ColumnBuilder<AbsNameBuilder<T>>,
        IndexBuilder<AbsNameBuilder<T>>,
        PrimaryKeyBuilder<T> {
}
