package org.mimosaframework.orm.sql;

public interface LimitCountBuilder<T> extends UnifyBuilder {
    T limit(int len);
}
