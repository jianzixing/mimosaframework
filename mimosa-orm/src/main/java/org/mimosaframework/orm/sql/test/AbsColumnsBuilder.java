package org.mimosaframework.orm.sql.test;

import java.io.Serializable;

public interface AbsColumnsBuilder<T> {
    T columns(Serializable... field);
}
