package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.AbsColumnBuilder;
import org.mimosaframework.orm.sql.AfterBuilder;
import org.mimosaframework.orm.sql.BeforeBuilder;
import org.mimosaframework.orm.sql.FirstBuilder;
import org.mimosaframework.orm.sql.create.ColumnAssistBuilder;

public interface AlterColumnAssistBuilder<T>
        extends
        FirstBuilder<AbsColumnBuilder<T>>,
        AfterBuilder<AbsColumnBuilder<T>>,
        BeforeBuilder<AbsColumnBuilder<T>>,

        ColumnAssistBuilder<AlterColumnAssistBuilder<T>> {
}
