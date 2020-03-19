package org.mimosaframework.orm.sql.test.alter;

import org.mimosaframework.orm.sql.test.*;

public interface AlterTableOptionBuilder
        extends
        AlterTableAnyBuilder,

        AutoIncrementBuilder<AbsIntBuilder>,
        CharsetBuilder,
        CollateBuilder,
        CommentBuilder,
        AbsExtraBuilder {
}
