package org.mimosaframework.orm.sql.alter;

import java.io.Serializable;

public interface AlterNewColumnBuilder<T> {
    T newColumn(Serializable field);
}
