package org.mimosaframework.orm.sql.test.alter;

import org.mimosaframework.orm.sql.test.*;
import org.mimosaframework.orm.sql.test.create.ColumnTypeBuilder;

public interface AlterAddAnyBuilder
        extends
        ColumnBuilder<AbsNameBuilder<ColumnTypeBuilder<AlterColumnAssistBuilder>>>,
        IndexBuilder<AbsNameBuilder<AlterColumnsBuilder<CommentBuilder>>>,
        UniqueBuilder<AbsNameBuilder<AlterColumnsBuilder<CommentBuilder>>>,
        FullTextBuilder<IndexBuilder<AbsNameBuilder<AlterColumnsBuilder<CommentBuilder>>>>,
        PrimaryKeyBuilder<AlterColumnsBuilder<CommentBuilder>> {
}
