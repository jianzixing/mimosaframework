package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.create.ColumnTypeBuilder;

public interface AlterAddAnyBuilder<T>
        extends
        AbsColumnBuilder<ColumnTypeBuilder<AlterColumnAssistBuilder<T>>>,
        IndexBuilder<AbsNameBuilder<AlterColumnsBuilder<CommentBuilder<UnifyBuilder>>>>,
        UniqueBuilder<AbsNameBuilder<AlterColumnsBuilder<CommentBuilder<UnifyBuilder>>>>,
        PrimaryKeyBuilder<AlterPKColumnBuilder<CommentBuilder<UnifyBuilder>>> {
}
