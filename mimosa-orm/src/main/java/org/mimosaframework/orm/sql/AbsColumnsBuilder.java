package org.mimosaframework.orm.sql;

import java.io.Serializable;

public interface AbsColumnsBuilder<T> {
    T columns(Serializable... fields);
}
