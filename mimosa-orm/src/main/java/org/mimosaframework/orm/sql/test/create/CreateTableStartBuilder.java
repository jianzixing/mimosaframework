package org.mimosaframework.orm.sql.test.create;

import org.mimosaframework.orm.sql.test.AbsNameBuilder;
import org.mimosaframework.orm.sql.test.INEBuilder;

public interface CreateTableStartBuilder
        extends
        INEBuilder<AbsNameBuilder<CreateTableColumnBuilder<CreateTableTailBuilder>>>,
        AbsNameBuilder<CreateTableColumnBuilder<CreateTableTailBuilder>> {
}
