package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.create.ColumnAssistBuilder;
import org.mimosaframework.orm.sql.create.ColumnTypeBuilder;

public interface RedefineAlterBuilder
        extends
        AlterBuilder,
        DatabaseBuilder,
        AbsNameBuilder,
        CharsetBuilder,
        CollateBuilder,
        AlterTableNameBuilder,
        AlterAddBuilder,
        ColumnBuilder,
        ColumnTypeBuilder,
        AutoIncrementBuilder,
        AbsColumnBuilder,
        CommentBuilder,
        AlterDropBuilder,
        AlterModifyBuilder,
        PrimaryBuilder,
        KeyBuilder,
        ToBuilder,
        AbsIntBuilder,
        AbsColumnsBuilder,
        AfterBuilder,
        FirstBuilder,
        BeforeBuilder,
        ColumnAssistBuilder {
}
