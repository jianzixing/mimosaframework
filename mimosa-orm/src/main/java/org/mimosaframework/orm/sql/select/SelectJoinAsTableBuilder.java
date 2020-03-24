package org.mimosaframework.orm.sql.select;

import org.mimosaframework.orm.sql.AsBuilder;
import org.mimosaframework.orm.sql.OnBuilder;

public interface SelectJoinAsTableBuilder<T>
        extends
        AsBuilder<OnBuilder<T>>,
        OnBuilder<T> {
}
