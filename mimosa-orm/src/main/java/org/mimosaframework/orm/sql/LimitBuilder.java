package org.mimosaframework.orm.sql;

public interface LimitBuilder<T> extends UnifyBuilder {
    T limit(long pos, long len);
}
