package org.mimosaframework.orm.sql.drop;

import org.mimosaframework.orm.sql.AbsTableBuilder;
import org.mimosaframework.orm.sql.IEBuilder;

public interface DropTableBuilder<T>
        extends
        IEBuilder<AbsTableBuilder<T>>,
        AbsTableBuilder<T> {
}
