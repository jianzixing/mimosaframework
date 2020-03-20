package org.mimosaframework.orm.sql.test;

import java.io.Serializable;

public interface FieldFunBuilder<T> {

    T count(Serializable column);

    T max(Serializable column);

    T avg(Serializable column);

    T sum(Serializable column);

    T min(Serializable column);

    T concat(Serializable... columnOrStr);

    T substring(Serializable columnOrStr, int pos, int len);
}
