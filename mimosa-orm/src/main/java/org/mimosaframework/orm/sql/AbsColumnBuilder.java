package org.mimosaframework.orm.sql;

import java.io.Serializable;

public interface AbsColumnBuilder<T> {
    T column(Serializable field);
}
