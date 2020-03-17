package org.mimosaframework.orm.sql.test;

public interface WhereItemBuilder<T> {
    T eq(Object key, Object value);

    T in(Object key, Iterable values);

    T in(Object key, Object... values);

    T nin(Object key, Iterable values);

    T nin(Object key, Object... values);

    T like(Object key, Object value);

    T ne(Object key, Object value);

    T gt(Object key, Object value);

    T gte(Object key, Object value);

    T lt(Object key, Object value);

    T lte(Object key, Object value);

    T between(Object key, Object start, Object end);

    T isNull(Object key);

    T isNotNull(Object key);
}
