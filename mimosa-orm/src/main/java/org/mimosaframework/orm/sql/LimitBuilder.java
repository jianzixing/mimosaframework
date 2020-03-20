package org.mimosaframework.orm.sql;

public interface LimitBuilder<T> {
    T limit(int pos, int len);
}
