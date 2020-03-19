package org.mimosaframework.orm.sql.test.alter;

import java.io.Serializable;

public interface AlterOldColumnBuilder<T> {
    T oldColumn(Serializable field);
}
