package org.mimosaframework.orm.sql.test;

import java.io.Serializable;

public interface AbsColumnBuilder<T> {
    T column(Serializable field);
}
