package org.mimosaframework.orm.sql.create;

import java.io.Serializable;

public interface CreateIndexColumnsBuilder<T> {
    T columns(Serializable... columns);
}
