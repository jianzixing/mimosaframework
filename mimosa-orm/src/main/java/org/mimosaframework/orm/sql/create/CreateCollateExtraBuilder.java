package org.mimosaframework.orm.sql.create;

import org.mimosaframework.orm.sql.AbsExtraBuilder;
import org.mimosaframework.orm.sql.CollateBuilder;

public interface CreateCollateExtraBuilder<T>
        extends
        CollateBuilder<AbsExtraBuilder<T>>,
        AbsExtraBuilder<T> {
}
