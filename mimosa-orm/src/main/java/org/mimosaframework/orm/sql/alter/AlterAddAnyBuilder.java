package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.create.ColumnTypeBuilder;

public interface AlterAddAnyBuilder<T>
        extends
        ColumnBuilder<AbsNameBuilder<ColumnTypeBuilder<AlterColumnAssistBuilder<T>>>>,
        IndexBuilder<AbsNameBuilder<AlterColumnsBuilder<CommentBuilder<UnifyBuilder>>>>,
        UniqueBuilder<AbsNameBuilder<AlterColumnsBuilder<CommentBuilder<UnifyBuilder>>>>,
        FullTextBuilder<IndexBuilder<AbsNameBuilder<AlterColumnsBuilder<CommentBuilder<UnifyBuilder>>>>>,
        PrimaryKeyBuilder<AlterColumnsBuilder<CommentBuilder<UnifyBuilder>>> {
}
