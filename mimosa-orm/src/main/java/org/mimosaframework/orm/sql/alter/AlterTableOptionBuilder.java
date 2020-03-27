package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.*;

public interface AlterTableOptionBuilder
        extends
        AlterTableAnyBuilder,

        AutoIncrementBuilder<AbsIntBuilder<UnifyBuilder>>,
        CharsetBuilder<UnifyBuilder>,
        CollateBuilder<UnifyBuilder>,
        CommentBuilder<UnifyBuilder>,
        AbsExtraBuilder<UnifyBuilder> {
}
