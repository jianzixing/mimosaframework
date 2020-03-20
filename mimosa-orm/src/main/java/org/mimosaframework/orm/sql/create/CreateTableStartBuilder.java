package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.AbsNameBuilder;
import org.mimosaframework.orm.sql.INEBuilder;

public interface CreateTableStartBuilder
        extends
        INEBuilder<AbsNameBuilder<CreateTableColumnBuilder<CreateTableTailBuilder>>>,
        AbsNameBuilder<CreateTableColumnBuilder<CreateTableTailBuilder>> {
}
