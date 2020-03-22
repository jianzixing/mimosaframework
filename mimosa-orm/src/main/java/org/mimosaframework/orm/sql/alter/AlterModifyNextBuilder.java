package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.AbsColumnBuilder;
import org.mimosaframework.orm.sql.create.ColumnTypeBuilder;

public interface AlterModifyNextBuilder<T>
        extends
        AbsColumnBuilder<ColumnTypeBuilder<AlterColumnAssistBuilder<T>>> {
}
