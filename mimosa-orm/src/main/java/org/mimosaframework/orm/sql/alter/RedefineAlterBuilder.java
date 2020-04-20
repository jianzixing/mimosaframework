package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.create.ColumnAssistBuilder;
import org.mimosaframework.orm.sql.create.ColumnTypeBuilder;
import org.mimosaframework.orm.sql.rename.RenameNewColumnBuilder;
import org.mimosaframework.orm.sql.rename.RenameOldColumnBuilder;

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
        AfterBuilder,
        AbsColumnBuilder,
        CommentBuilder,
        AlterDropBuilder,
        AlterModifyBuilder,
        PrimaryBuilder,
        KeyBuilder,
        ToBuilder,
        AbsIntBuilder,
        AbsColumnsBuilder,
        ColumnAssistBuilder {
}
