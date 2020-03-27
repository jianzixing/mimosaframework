package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.AbsColumnBuilder;
import org.mimosaframework.orm.sql.AbsTableNameBuilder;
import org.mimosaframework.orm.sql.INEBuilder;
import org.mimosaframework.orm.sql.UnifyBuilder;

public interface CreateTableStartBuilder
        extends
        INEBuilder<AbsTableNameBuilder<AbsColumnBuilder<ColumnTypeBuilder<CreateColumnAssistBuilder<UnifyBuilder>>>>>,
        AbsTableNameBuilder<AbsColumnBuilder<ColumnTypeBuilder<CreateColumnAssistBuilder<UnifyBuilder>>>> {
}
