package org.mimosaframework.orm.sql.test.delete;

import org.mimosaframework.orm.sql.test.AbsTableBuilder;

public interface BeforeDeleteFromBuilder<T> extends AbsTableBuilder<T> {
    T tables(Class... table);

    T tables(String... aliasNames);
}
