package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.AbsColumnBuilder;
import org.mimosaframework.orm.sql.CommentBuilder;
import org.mimosaframework.orm.sql.PrimaryKeyBuilder;
import org.mimosaframework.orm.sql.UnifyBuilder;
import org.mimosaframework.orm.sql.create.ColumnTypeBuilder;

public interface AlterAddAnyBuilder<T>
        extends
        AbsColumnBuilder<ColumnTypeBuilder<AlterColumnAssistBuilder<T>>>,
        PrimaryKeyBuilder<AlterPKColumnBuilder<CommentBuilder<UnifyBuilder>>> {
}
