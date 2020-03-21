package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.*;

public interface AlterDropAnyBuilder
        extends
        ColumnBuilder<AbsNameBuilder<UnifyBuilder>>,
        IndexBuilder<AbsNameBuilder<UnifyBuilder>>,
        PrimaryKeyBuilder<UnifyBuilder> {
}
