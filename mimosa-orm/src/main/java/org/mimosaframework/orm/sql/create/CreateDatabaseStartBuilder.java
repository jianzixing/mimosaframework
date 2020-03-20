package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.CollateBuilder;
import org.mimosaframework.orm.sql.CharsetBuilder;
import org.mimosaframework.orm.sql.AbsNameBuilder;
import org.mimosaframework.orm.sql.INEBuilder;

public interface CreateDatabaseStartBuilder
        extends
        INEBuilder<AbsNameBuilder<CharsetBuilder<CollateBuilder>>>,
        AbsNameBuilder<CharsetBuilder<CollateBuilder>> {
}
