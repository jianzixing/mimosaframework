package org.mimosaframework.orm.sql.drop;

import org.mimosaframework.orm.sql.AbsNameBuilder;
import org.mimosaframework.orm.sql.IEBuilder;

public interface DropDatabaseBuilder<T>
        extends
        IEBuilder<AbsNameBuilder<T>>,
        AbsNameBuilder<T> {
}
