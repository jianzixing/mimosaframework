package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.*;

public interface AlterTableOptionBuilder
        extends
        AlterTableAnyBuilder,

        AutoIncrementBuilder<AbsIntBuilder>,
        CharsetBuilder,
        CollateBuilder,
        CommentBuilder,
        AbsExtraBuilder {
}
