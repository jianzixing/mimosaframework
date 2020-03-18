package org.mimosaframework.orm.sql.test.create;

import org.mimosaframework.orm.sql.test.CharsetBuilder;
import org.mimosaframework.orm.sql.test.CollateBuilder;
import org.mimosaframework.orm.sql.test.INFBuilder;

public interface CreateDatabaseStartBuilder
        extends
        INFBuilder<CreateNameBuilder<CharsetBuilder<CollateBuilder>>>,
        CreateNameBuilder<CharsetBuilder<CollateBuilder>> {
}
