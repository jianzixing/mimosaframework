package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.create.ColumnTypeBuilder;

public interface AlterAddAnyBuilder<T>
        extends
        AbsColumnBuilder<ColumnTypeBuilder<AlterColumnAssistBuilder<T>>>,
        PrimaryKeyBuilder<AlterPKColumnBuilder<CommentBuilder<UnifyBuilder>>> {
}
