package org.mimosaframework.orm.sql;

public interface LimitBuilder<T> extends UnifyBuilder {
    T limit(int pos, int len);
}
