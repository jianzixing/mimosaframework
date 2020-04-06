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
        AbsTableBuilder,
        AlterAddBuilder,
        ColumnBuilder,
        ColumnTypeBuilder,
        AutoIncrementBuilder,
        AfterBuilder,
        AbsColumnBuilder,
        IndexBuilder,
        CommentBuilder,
        SplitBuilder,
        AlterDropBuilder,
        AlterModifyBuilder,
        AlterChangeBuilder,
        AlterOldColumnBuilder,
        AlterNewColumnBuilder,
        PrimaryBuilder,
        KeyBuilder,
        AlterRenameBuilder,
        ToBuilder,
        UniqueBuilder,
        AbsIntBuilder,
        AbsColumnsBuilder,
        ColumnAssistBuilder {
}
