package org.mimosaframework.orm.sql.alter;

import java.io.Serializable;

public interface AlterOldColumnBuilder<T> {
    T oldColumn(Serializable field);
}
