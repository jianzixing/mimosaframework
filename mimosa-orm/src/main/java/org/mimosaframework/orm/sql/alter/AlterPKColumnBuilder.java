package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.AbsNameBuilder;

public interface AlterPKColumnBuilder<T>
        extends
        AlterColumnsBuilder<T>,
        AbsNameBuilder<AlterColumnsBuilder<T>> {
}
