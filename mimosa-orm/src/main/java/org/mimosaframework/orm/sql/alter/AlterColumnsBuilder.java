package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.AbsColumnBuilder;
import org.mimosaframework.orm.sql.AbsColumnsBuilder;

public interface AlterColumnsBuilder<T>
        extends
        AbsColumnBuilder<T>,
        AbsColumnsBuilder<T> {
}
