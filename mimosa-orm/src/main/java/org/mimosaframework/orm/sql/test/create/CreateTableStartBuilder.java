package org.mimosaframework.orm.sql.test.create;

import org.mimosaframework.orm.sql.test.INFBuilder;

public interface CreateTableStartBuilder
        extends
        INFBuilder<CreateNameBuilder<CreateTableColumnBuilder<CreateTableTailBuilder>>>,
        CreateNameBuilder<CreateTableColumnBuilder<CreateTableTailBuilder>> {
}
