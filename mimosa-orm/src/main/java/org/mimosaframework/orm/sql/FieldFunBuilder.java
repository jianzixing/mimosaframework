package org.mimosaframework.orm.sql;

import java.io.Serializable;

public interface FieldFunBuilder<T> {
    enum Support {
        COUNT, MAX, AVG, SUM, MIN, CONCAT, SUBSTRING
    }

    T count(Serializable... param);

    T max(Serializable... params);

    T avg(Serializable... params);

    T sum(Serializable... params);

    T min(Serializable... params);

    T concat(Serializable... params);

    T substring(Serializable param, int pos, int len);
}
