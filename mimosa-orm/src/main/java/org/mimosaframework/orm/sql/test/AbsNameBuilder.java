package org.mimosaframework.orm.sql.test;

import java.io.Serializable;

public interface AbsNameBuilder<T> {
    T name(Serializable value);
}
