package org.mimosaframework.orm.sql;

import java.io.Serializable;

public interface AbsNameBuilder<T> {
    T name(Serializable value);
}
