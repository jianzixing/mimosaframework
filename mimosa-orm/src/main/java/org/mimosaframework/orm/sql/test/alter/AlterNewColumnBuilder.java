package org.mimosaframework.orm.sql.test.alter;

import java.io.Serializable;

public interface AlterNewColumnBuilder<T> {
    T newColumn(Serializable field);
}
