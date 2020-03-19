package org.mimosaframework.orm.sql.test.alter;

import org.mimosaframework.orm.sql.test.AbsNameBuilder;
import org.mimosaframework.orm.sql.test.AfterBuilder;
import org.mimosaframework.orm.sql.test.FirstBuilder;
import org.mimosaframework.orm.sql.test.create.ColumnAssistBuilder;

public interface AlterColumnAssistBuilder
        extends
        FirstBuilder<AbsNameBuilder>,
        AfterBuilder<AbsNameBuilder>,

        ColumnAssistBuilder<AlterColumnAssistBuilder> {
}
