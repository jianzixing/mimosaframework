package org.mimosaframework.orm.sql.test.create;

import java.io.Serializable;

public interface CreateIndexColumnsBuilder<T> {
    T columns(Serializable... columns);
}
