package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.create.ColumnTypeBuilder;

public interface AlterAddAnyBuilder
        extends
        ColumnBuilder<AbsNameBuilder<ColumnTypeBuilder<AlterColumnAssistBuilder>>>,
        IndexBuilder<AbsNameBuilder<AlterColumnsBuilder<CommentBuilder>>>,
        UniqueBuilder<AbsNameBuilder<AlterColumnsBuilder<CommentBuilder>>>,
        FullTextBuilder<IndexBuilder<AbsNameBuilder<AlterColumnsBuilder<CommentBuilder>>>>,
        PrimaryKeyBuilder<AlterColumnsBuilder<CommentBuilder>> {
}
