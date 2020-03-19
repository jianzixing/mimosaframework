package org.mimosaframework.orm.sql.test.create;

import org.mimosaframework.orm.sql.test.CharsetBuilder;
import org.mimosaframework.orm.sql.test.CollateBuilder;
import org.mimosaframework.orm.sql.test.AbsNameBuilder;
import org.mimosaframework.orm.sql.test.INEBuilder;

public interface CreateDatabaseStartBuilder
        extends
        INEBuilder<AbsNameBuilder<CharsetBuilder<CollateBuilder>>>,
        AbsNameBuilder<CharsetBuilder<CollateBuilder>> {
}
