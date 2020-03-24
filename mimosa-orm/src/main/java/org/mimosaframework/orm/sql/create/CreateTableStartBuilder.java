package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.AbsTableNameBuilder;
import org.mimosaframework.orm.sql.INEBuilder;

public interface CreateTableStartBuilder
        extends
        INEBuilder<AbsTableNameBuilder<CreateTableColumnBuilder<CreateTableTailBuilder>>>,
        AbsTableNameBuilder<CreateTableColumnBuilder<CreateTableTailBuilder>> {
}
